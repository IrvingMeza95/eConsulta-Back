package com.iamf.servicioVerificacion.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.dtos.RequestDTO;
import com.iamf.servicioVerificacion.services.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {
	
	@Autowired
	private EmailServiceImpl emailService;

	@PostMapping("/bienvenida")
	public ResponseEntity<?> emailDeBienvenida(@RequestBody RequestDTO request) throws MyException {
		return emailService.emailDeBienvenida(request);
	}

	@PostMapping("/nuevo-login")
	public ResponseEntity<?> nuevoLogin(@RequestBody RequestDTO request) throws MyException {
		return emailService.nuevoLogin(request);
	}

	@PostMapping("/enviar-archivo")
	public ResponseEntity<?> enviarArchivo(@RequestBody RequestDTO request) throws MyException {
		return emailService.enviarArchivo(request);
	}

}
