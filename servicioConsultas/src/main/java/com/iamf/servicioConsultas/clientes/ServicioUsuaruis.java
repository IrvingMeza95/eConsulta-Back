package com.iamf.servicioConsultas.clientes;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.models.Turno;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="servicioUsuarios")
public interface ServicioUsuaruis {
	@GetMapping("/persona/{param}")
	PersonaDTO getPersona(@PathVariable String param);
	@GetMapping("/turnos/get-all")
	List<Turno> getTurnos(@RequestParam(required = false) String horario,
						  @RequestParam(required = false) String medicoEmail);
}
