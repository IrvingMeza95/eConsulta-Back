package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Persona;
import com.iamf.commons.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crear(Usuario usuario) throws MyException;
    Usuario guardar(Usuario usuario) throws MyException;
    Usuario getUsuario(String param) throws MyException;
    Persona getPersona(String param) throws MyException;
    Usuario modificar(String paramUsuario, UsuarioDTO nuevoUsuario) throws MyException;
    void eliminar(String param) throws MyException;
    void deshabilitarHabilitar(String param) throws MyException;
    void cambairPassword(String password, String nuevaPassword, String param) throws MyException;
    List<Persona> getPersonas();
    Boolean verificar(String param, String template, Integer codigo, String fecha) throws MyException;
    Integer guardarCodigoDeVerificacion(String email, Integer codigo, String fechaDeExpiracion);
    TipoPersona getTipoPersona(String param) throws MyException;
    void agregarPassword(String param, String password, Integer codigo, String fecha) throws MyException;
}
