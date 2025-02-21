package com.iamf.servicioVerificacion.controllers;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.dtos.RequestDTO;
import com.iamf.servicioVerificacion.clientes.ServicioUsuarios;
import com.iamf.servicioVerificacion.services.impl.EmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@Slf4j
public class EmailController {
	
	@Autowired
	private EmailServiceImpl emailService;
	@Autowired
	private ServicioUsuarios servicioUsuarios;

	@PostMapping("/bienvenida")
	public ResponseEntity<?> emailDeBienvenida(@RequestBody RequestDTO request) throws MyException {
		return emailService.emailDeBienvenida(request);
	}

	@PostMapping("/nuevo-login")
	public ResponseEntity<?> nuevoLogin(@RequestBody RequestDTO request) throws MyException {
		UsuarioDTO usuario = new UsuarioDTO();
		try{
			usuario = servicioUsuarios.getUsuario(request.getTo());
		}catch (RuntimeException e){
			log.error(e.getMessage());
			throw new RuntimeException("Error al obtener al usuario con el email " + request.getTo() + ".");
		}
		return emailService.nuevoLogin(request, usuario);
	}

	@PostMapping("/enviar-archivo")
	public ResponseEntity<?> enviarArchivo(@RequestBody RequestDTO request) throws MyException {
		return emailService.enviarArchivo(request);
	}

}
