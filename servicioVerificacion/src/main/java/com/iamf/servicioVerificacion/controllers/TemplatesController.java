package com.iamf.servicioVerificacion.controllers;

import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.exceptions.MyException;
import com.iamf.servicioVerificacion.services.impl.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/templates")
public class TemplatesController {

	@Autowired
	private TemplateService templateService;

	@PostMapping
	public ResponseEntity<?> saveNewTemplate(@RequestParam("file") MultipartFile templateFile ,
											 @RequestParam("tipo") String tipo , @RequestParam("description")
											 String description , @RequestParam("vars") List<String> vars) throws MyException {
		return templateService.saveNewTemplate(templateFile, tipo, description, vars);
	}

	@GetMapping("/tipos-de-plantillas")
	public TiposDePlantillas[] tiposDePlantillas(){
		return TiposDePlantillas.values();
	}

}
