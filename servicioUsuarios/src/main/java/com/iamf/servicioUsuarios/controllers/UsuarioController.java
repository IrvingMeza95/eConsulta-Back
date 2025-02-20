package com.iamf.servicioUsuarios.controllers;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.mappers.PersonaMapper;
import com.iamf.commons.mappers.UsuarioMapper;
import com.iamf.commons.models.Persona;
import com.iamf.commons.models.Usuario;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.services.interfaces.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.rmi.server.LogStream.log;

@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    private final PersonaMapper personaMapper = new PersonaMapper();
    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

    @GetMapping("/credenciales/{param}")
    public UsuarioDTO getUsuario(@PathVariable String param) throws MyException {
        log.info("Credenciales para verificar: " + param);
        return usuarioMapper.getUsuarioDTO(usuarioService.getPersona(param));
    }

    @GetMapping("/{param}")
    public ResponseEntity<Persona> getPersona(@PathVariable String param) throws MyException {
        return ResponseEntity.ok(usuarioService.getPersona(param));
    }

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> getPersonas(){
        return ResponseEntity.ok(personaMapper.listaPersonaDTO(usuarioService.getPersonas()));
    }

    @PutMapping("/{param}")
    public ResponseEntity<Usuario> guardar(@PathVariable String param, @RequestBody Usuario usuario) throws MyException {
        log.info("Guardando credenciales.");
        usuario.setPersona(usuarioService.getPersona(usuario.getEmail()));
        return ResponseEntity.ok(usuarioService.guardar(usuario));
    }

    @PutMapping("/cambiar-password/{param}")
    public ResponseEntity<ResponseMessage> cambiarPassword(@RequestParam String password, @RequestParam String nuevaPassword,
                                                  @PathVariable String param) throws MyException {
        usuarioService.cambairPassword(password,nuevaPassword, param);
        return ResponseEntity.ok(new ResponseMessage("Contraseña actualizada con éxito."));
    }

    @PutMapping("/deshabilitar-habilitar/{param}")
    public ResponseEntity<ResponseMessage> deshabilitarHabbilitar(@PathVariable String param) throws MyException {
        usuarioService.deshabilitarHabilitar(param);
        return ResponseEntity.ok(new ResponseMessage("Acciión realizada correctamente."));
    }

    @PutMapping("/guardar-codigo-verificacion")
    public Integer guardarCodigoDeVerificacion(@RequestParam String email, @RequestParam Integer codigo, @RequestParam String fechaDeExpiracion){
        return usuarioService.guardarCodigoDeVerificacion(email,codigo, fechaDeExpiracion);
    }

    @PutMapping("/agregar-password/{param}")
    public ResponseEntity<ResponseMessage> agregarPassword(@PathVariable String param, @RequestParam String password,
                                                           @RequestParam Integer codigo, @RequestParam String fecha) throws MyException {
        usuarioService.agregarPassword(param,password,codigo, fecha);
        return  ResponseEntity.ok(new ResponseMessage("Contraseña actualizada exitósamente."));
    }

    @GetMapping("/tipo-persona/{param}")
    public ResponseEntity<TipoPersona> getTipoPersona(@PathVariable String param) throws MyException {
        return ResponseEntity.ok(usuarioService.getTipoPersona(param));
    }

    @PutMapping("/modificar-roles/{email}")
    public ResponseEntity<ResponseMessage> modificarRoles(@PathVariable String email,
            @RequestBody List<Long> rolesIds) throws MyException {
        return ResponseEntity.ok(usuarioService.modificarRoles(email,rolesIds));
    }

}
