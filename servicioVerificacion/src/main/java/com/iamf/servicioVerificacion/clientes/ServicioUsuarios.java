package com.iamf.servicioVerificacion.clientes;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="servicioUsuarios")
public interface ServicioUsuarios {
    @GetMapping("/usuarios/credenciales/{param}")
    UsuarioDTO getUsuario(@PathVariable String param);
    @PutMapping("/persona/{param}")
    ResponseEntity<PersonaDTO> modificarPersona(@PathVariable String param, @RequestBody PersonaDTO personaDTO);
    @PutMapping("/usuarios/guardar-codigo-verificacion")
    Integer guardarCodigoDeVerificacion(@RequestParam String email, @RequestParam Integer codigo, @RequestParam String fechaDeExpiracion);
}
