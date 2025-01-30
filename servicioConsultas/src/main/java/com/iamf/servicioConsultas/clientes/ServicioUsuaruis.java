package com.iamf.servicioConsultas.clientes;

import com.iamf.commons.dtos.PersonaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="servicioUsuarios")
public interface ServicioUsuaruis {
	@GetMapping("/persona/{param}")
	PersonaDTO getPersona(@PathVariable String param);
}
