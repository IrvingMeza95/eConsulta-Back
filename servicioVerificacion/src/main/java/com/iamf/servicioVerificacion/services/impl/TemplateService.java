package com.iamf.servicioVerificacion.services.impl;

import com.iamf.commons.dtos.MetaData;
import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.servicioVerificacion.dtos.Body;
import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.models.Template;
import com.iamf.servicioVerificacion.repositories.TemplateRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Service
@Slf4j
public class TemplateService {

	final Logger LOG = Logger.getLogger("com.confety.mail.service.TemplateService.class");
	
	private static final String CONTENT_TYPE = "text/html";
	
	@Autowired
	private TemplateRepo templateRepo;

	public Template buscarPorTipo(String tipo) throws MyException{
		Optional<Template> template = templateRepo.buscarPorTipo(tipo);
		if (template.isEmpty())
			throw new MyException("La plantilla del tipo "+ tipo + " no existe.");
		return template.get();
	}

	public ResponseEntity<?> saveNewTemplate(MultipartFile templateFile, String tipo, String description,
											 List<String> vars) throws MyException {
		LOG.info("Validando si el tipo de plantilla existe...");
		TiposDePlantillas.validarExistencia(tipo);
		LOG.info("El tipo de planatilla fue validado.");
		Optional<Template> template = templateRepo.buscarPorTipo(tipo);
		if (template.isPresent()){
			LOG.info("Se encontro una plantilla del tipo " + tipo + ", se procedera a actualizar.");
			return crearPlantilla(template.get(),templateFile,TiposDePlantillas.valueOf(tipo),description,vars);
		}else{
			LOG.info("No se encontro alguna plantilla del tipo " + tipo + ", se creara una nueva.");
			Template nuevaTemplate = new Template();
			return crearPlantilla(nuevaTemplate,templateFile,TiposDePlantillas.valueOf(tipo),description,vars);
		}
    }

	public ResponseEntity<?> crearPlantilla(Template template, MultipartFile templateFile, TiposDePlantillas tipo,
											String description, List<String> vars){

        try {
			LOG.info("Creando nueva plantilla...");
			if(validateContentType(templateFile)) {
                LOG.info("Documento validado como html.");
				template.setContent(new String(templateFile.getBytes()));
			}else if ("text/plain".equals(templateFile.getContentType()) &&
					templateFile.getOriginalFilename() != null && templateFile.getOriginalFilename().toLowerCase().endsWith(".txt")){
				LOG.info("Se creara un archivo tipo txt.");
				template.setContent(new String(templateFile.getBytes(), StandardCharsets.UTF_8));
			}else {
				LOG.info("El documento no es html.");
                return new ResponseEntity<Body>(new Body("Error al procesar el archivo, solo se admiten html con contenido.") , HttpStatus.NOT_ACCEPTABLE);
            }
			log.info("Descripcion: " + description);
			template.setDescription(description);
			log.info("Tipo: " + tipo);
			template.setTipo(tipo.name());
			template.setVars(vars.toString());
			templateRepo.save(template);
			return new ResponseEntity<Body>(new Body("La template fue creada exitosamente con Id --> " + template.getId()) , HttpStatus.OK);
        } catch (IOException e) {
			return new ResponseEntity<Body>(new Body(e.getMessage()) , HttpStatus.INTERNAL_SERVER_ERROR);}
    }
	
	private Boolean validateContentType(MultipartFile templateFile) throws IOException {
		if(templateFile != null && CONTENT_TYPE.equals(templateFile.getContentType()) && templateFile.getSize() > 0) {
			LOG.info("Template cumple con los requisitos para ser procesada.");
			return true;
		}
		LOG.warning("Error al procesar el archivo.");
		return false;
	}

	public String buildMessage(RequestDTO reqquest) throws MyException {
		try {
			TiposDePlantillas.validarExistencia(reqquest.getTemplate());
			Template template = buscarPorTipo(
					reqquest.getTemplate());

			List<String> keys = new ArrayList<>();
			String finalMessage = template.getContent();
			for (MetaData meta : reqquest.getMetaData()) {
				if(template.getVars().contains(meta.getKey())) {
					log.info("Se reempplazo: " + meta.getKey());
					finalMessage = finalMessage.replace("$[{" + meta.getKey() + "}]", meta.getValue());
				}else {
					throw new NotAcceptableStatusException("MetaData incompleta debe contener -> "+ template.getVars());
				}
			}

			return finalMessage;
		}catch(MyException e) {
			throw new MyException(e.getMessage());
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
