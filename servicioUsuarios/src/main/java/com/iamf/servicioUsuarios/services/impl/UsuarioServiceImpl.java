package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.enums.NivelDeVerificacion;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Persona;
import com.iamf.commons.models.Usuario;
import com.iamf.commons.utils.EmailUtils;
import com.iamf.commons.utils.ServicioVerificacionUtils;
import com.iamf.servicioUsuarios.clientes.ServicioVerificacion;
import com.iamf.servicioUsuarios.repositories.UsuarioRepo;
import com.iamf.servicioUsuarios.services.interfaces.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private ServicioVerificacion servicioVerificacion;

    @Override
    public Usuario crear(Usuario usuario) throws MyException {
//        if (usuario.getPassword() == null || usuario.getPassword().equals(""))
//            throw new MyException("La contraseña no puede ser nula.");
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        usuario.setPassword(encoder.encode(usuario.getPassword()));
        usuario.setUsername(usuario.getEmail());
//        usuario.setCodigoDeVerificacion(ServicioVerificacionUtils.codigoDeVerificacion());
        return guardar(usuario);
    }

    @Override
    public Usuario guardar(Usuario usuario) throws MyException {
        validarDatos(usuario);
        return usuarioRepo.save(usuario);
    }

    @Override
    public Usuario getUsuario(String param) throws MyException {
        if (param.isEmpty())
            throw new MyException("Parámetro no especificado.");
        Optional<Usuario> usuario = usuarioRepo.buscarPorIdEmailCelular(param);
        if (usuario.isEmpty())
            throw new MyException("User not found.");
        return usuario.get();
    }

    @Override
    public Persona getPersona(String param) throws MyException {
        Usuario usuario = getUsuario(param);
        return usuario.getPersona();
    }

    @Override
    public Usuario modificar(String paramUsuario, UsuarioDTO nuevoUsuario) throws MyException {
        Usuario usuario = getUsuario(paramUsuario);
        if (nuevoUsuario.getCodigoDeLlamada() != null)
            usuario.setCodigoDeLlamada(nuevoUsuario.getCodigoDeLlamada());
        if (nuevoUsuario.getCelular() != null)
            usuario.setCelular(nuevoUsuario.getCelular());
        if (nuevoUsuario.getRoles() != null)
            usuario.setRoles(nuevoUsuario.getRoles());
        if (nuevoUsuario.getEnabled() != null)
            usuario.setEnabled(nuevoUsuario.getEnabled());
        if (nuevoUsuario.getIntentos() != null)
            usuario.setIntentos(nuevoUsuario.getIntentos());
        if (nuevoUsuario.getNivelDeVerificacion() != null) {
            NivelDeVerificacion.validar(nuevoUsuario.getNivelDeVerificacion().name());
            usuario.setNivelDeVerificacion(nuevoUsuario.getNivelDeVerificacion());
        }
        if (nuevoUsuario.getVerificacion2Factores() != null)
            usuario.setVerificacion2Factores(nuevoUsuario.getVerificacion2Factores());
        if (nuevoUsuario.getVerificacion2Factores() != null)
            usuario.setVerificacion2Factores(nuevoUsuario.getVerificacion2Factores());
        if (nuevoUsuario.getEmailVerificado() != null)
            usuario.setEmailVerificado(nuevoUsuario.getEmailVerificado());
        if (nuevoUsuario.getCelularVerificado() != null)
            usuario.setCelularVerificado(nuevoUsuario.getCelularVerificado());
        if (nuevoUsuario.getCodigoDeVerificacion() != null)
            usuario.setCodigoDeVerificacion(nuevoUsuario.getCodigoDeVerificacion());
        return usuario;
    }

    @Override
    public void eliminar(String param) throws MyException {
        Usuario usuario = getUsuario(param);
        usuarioRepo.delete(usuario);
    }

    @Override
    public void deshabilitarHabilitar(String param) throws MyException {
        Usuario usuario = getUsuario(param);
        usuario.setEnabled(!usuario.getEnabled());
        usuario.setPersona(getPersona(param));
        if (usuario.getPersona() == null)
            throw new MyException("Error al mantener la relacián jdel usuario y sus cdenciales.");
        guardar(usuario);
    }

    @Override
    public void cambairPassword(String password, String nuevaPassword, String param) throws MyException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Usuario usuarioBd = getUsuario(param);
        if (password == null || password.isEmpty())
            throw new MyException("Contraseña inválida.");
        if (!encoder.matches(password,usuarioBd.getPassword()))
            throw new MyException("La contraseña actual tecleada no coincide con la guardada.");
        usuarioBd.setPassword(encoder.encode(nuevaPassword));
        guardar(usuarioBd);
    }

    @Override
    public List<Persona> getPersonas() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        return usuarios.stream().map(Usuario::getPersona).collect(Collectors.toList());
    }

    @Override
    public Boolean verificar(String param, String template, Integer codigo, String fecha) throws MyException {
        TiposDePlantillas.validarExistencia(template);
        Usuario usuario = getUsuario(param);
        if (usuario.getCodigoDeVerificacion() != null && usuario.getCodigoDeVerificacion().equals(codigo)){
            if (TiposDePlantillas.CODIGO_VERIFICACION_DE_CORREO.name().equals(template)){
                verificarCorreo(usuario);
            }else if (TiposDePlantillas.CODIGO_VERIFICACION_DE_CELULAR.name().equals(template)){
                verificarCelular(usuario);
            }else{
                verificacionDe2Factores(usuario,fecha);
            }
            log.info("Usuario " + usuario.getUsername() + " ha sido verificado correctamente.");
            return true;
        }else{
            log.error("Error al verificar al usuario " + usuario.getUsername() + ".");
            return false;
        }
    }

    private void cambiarNivelDeVerificacion(Usuario usuario, NivelDeVerificacion nivel) throws MyException {
        //Cambiar el nivel de verificacion al concluir el registro y verificacion de correo
        if (usuarioRepo.cambiarNivelDeVerificacion(nivel, usuario.getEmail()) < 1) {
            log.error("Algo salio mal en el guardado a la base de datos.");
            throw new MyException("Error en el proceso de verificación.");
        }
    }

    private void eliminarCodigoDeVerificacion(Usuario usuario) throws MyException {
        log.info("Eliminando codigo de base de datos.");
        if (usuarioRepo.eliminarCodigoDeVerificacion(usuario.getEmail()) < 1){
            log.error("Algo salio mal en la eliminacion de la base de datos.");
            throw new MyException("Error en el proceso de verificación.");
        }else{
            log.info("Codigo eliminado de base de datos correctamente.");
        }
    }

    private void verificarCorreo(Usuario usuario) throws MyException {
        if (usuario.getNivelDeVerificacion().equals(NivelDeVerificacion.SIN_VERIFICAR)){
            cambiarNivelDeVerificacion(usuario,NivelDeVerificacion.BASICO);
            log.info("Se envió correo de bienvenida.");
            RequestDTO emailSendRequest = RequestDTO.builder()
                    .to(usuario.getEmail())
                    .build();
            servicioVerificacion.emailDeBienvenida(emailSendRequest);
        }else{
            log.info("Se envio un correo de verificacion de correo.");
            //Mensaje de correo verificado correctamente
        }
        usuarioRepo.cambiarEmailVerificado(true,usuario.getEmail());
        eliminarCodigoDeVerificacion(usuario);
    }

    private void verificarCelular(Usuario usuario) throws MyException {
        log.info("Verificando numero de celular.");
        //Se enviara un mensaje de exito
        usuarioRepo.cambiarCelularVerificado(true,usuario.getEmail());
        eliminarCodigoDeVerificacion(usuario);
    }

    private void verificacionDe2Factores(Usuario usuario, String fecha) throws MyException {
        log.info("Realizando verificacion de 2 factores.");
        if (verificarExpiracionDeCodigo(usuario.getVencimientoDeCodigoDeVerificacion(), fecha)){
            log.info("El codigo esta expirado.");
            throw new MyException("El código há éxpirado.");
        }
        eliminarCodigoDeVerificacion(usuario);
    }

    @Override
    public Integer guardarCodigoDeVerificacion(String email, Integer codigo, String fechaDeExpiracion) {
        return usuarioRepo.guardarCodigoDeVerificacion(codigo,email, fechaDeExpiracion);
    }

    @Override
    public TipoPersona getTipoPersona(String param) throws MyException {
        Optional<TipoPersona> tipoPersona = usuarioRepo.getTipoPersona(param);
        if (tipoPersona.isEmpty())
            throw new MyException("User not found.");
        return tipoPersona.get();
    }

    private void validarDatos(Usuario usuario) throws MyException {
        if (!EmailUtils.isValidEmail(usuario.getEmail()))
            throw new MyException("La dirección de correo no es válida.");
        if (usuario.getUsername() == null || usuario.getUsername().equals(" "))
            throw new MyException("El nombre de usuario de no es válido.");
        if (usuario.getCodigoDeLlamada() == null || usuario.getCodigoDeLlamada().equals(" "))
            throw new MyException("El codigo de llamada no es válido.");
        if (usuario.getCelular() == null || usuario.getCelular().equals(" "))
            throw new MyException("El celular no es válido.");
        if (usuario.getRoles() == null)
            throw new MyException("Es necesario asignar al menos un role al usuario.");
    }

    private Boolean verificarExpiracionDeCodigo(String dbFecha, String fechaaRequest){
        // Definir el formato deseado para la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Obtener la fecha y hora actual
        LocalDateTime fecha1 = LocalDateTime.parse(dbFecha, formatter);
        LocalDateTime fecha2 = LocalDateTime.parse(fechaaRequest, formatter);
        if (fecha2.isAfter(fecha1)){
            return true;
        }else return false;
    }

    public void agregarPassword(String param, String password, Integer codigo, String fecha) throws MyException {
        Usuario usuario = getUsuario(param);
        if (verificarExpiracionDeCodigo(usuario.getVencimientoDeCodigoDeVerificacion(), fecha)){
            log.info("El codigo esta expirado.");
            throw new MyException("El código há éxpirado.");
        }
        if (codigo == null || !Objects.equals(usuario.getCodigoDeVerificacion(), codigo)){
            log.error("Error en el codigo de verificacion.");
            throw new MyException("Error al actualizar la contraseña.");
        }
        if (password == null || password.equals(""))
            throw new MyException("La contraseña no puede ser nula.");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setPassword(encoder.encode(password));
        usuario.setEnabled(true);
        guardar(usuario);
        eliminarCodigoDeVerificacion(usuario);
        usuarioRepo.cambiarEmailVerificado(true,param);
        cambiarNivelDeVerificacion(usuario,NivelDeVerificacion.BASICO);
    }

}
