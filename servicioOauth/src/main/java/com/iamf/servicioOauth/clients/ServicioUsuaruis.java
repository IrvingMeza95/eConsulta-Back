package com.iamf.servicioOauth.clients;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="servicioUsuarios")
public interface ServicioUsuaruis {
	@GetMapping("/usuarios/credenciales/{param}")
	UsuarioDTO getUsuario(@RequestParam String param);
	@PutMapping("/usuarios/{param}")
	Usuario guardar(@RequestBody UsuarioDTO usuario, @PathVariable String param);
}
