//package com.iamf.servicioUsuarios.controllers;
//
//import com.iamf.commons.dtos.RequestDTO;
//import com.iamf.commons.dtos.PersonaDTO;
//import com.iamf.commons.enums.TiposDePlantillas;
//import com.iamf.commons.exceptions.MyException;
//import com.iamf.commons.mappers.PersonaMapper;
//import com.iamf.commons.models.PersonaFisica;
//import com.iamf.servicioUsuarios.clientes.ServicioVerificacion;
//import com.iamf.servicioUsuarios.dtos.RegistroDTO;
//import com.iamf.servicioUsuarios.services.interfaces.PersonaFisicaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/persona-fisica")
//public class PersonaFisicaController {
//
//    @Autowired
//    private PersonaFisicaService personaFisicaService;
//    private final PersonaMapper personaMapper = new PersonaMapper();
//    @Autowired
//    private ServicioVerificacion servicioVerificacion;
//
//    @PostMapping
//    public ResponseEntity<PersonaDTO> crear(@RequestBody RegistroDTO registro) throws MyException {
//        PersonaFisica personaFisica = personaFisicaService.crear(registro);
//        RequestDTO request = new RequestDTO();
//        request.setTo(personaFisica.getCredenciales().getEmail());
//        request.setTemplate(TiposDePlantillas.CODIGO_VERIFICACION_DE_CORREO.name());
//        servicioVerificacion.codigoDeVerificacion(request);
//        return ResponseEntity.ok(personaMapper.getPersonaFisicaDTO(personaFisica));
//    }
//
////    @PutMapping("/{param}")
////    public ResponseEntity<PersonaDTO> modificar(@PathVariable String param, @RequestBody PersonaFisica personaFisica) throws MyException {
////        return ResponseEntity.ok(
////                personaMapper.getPersonaFisicaDTO(personaFisicaService.modificar(param, personaFisica))
////        );
////    }
//
//    @PutMapping("/agregar-archivo/{param}/{idArchivo}")
//    public void agregarArchivo(@PathVariable String param, @PathVariable String idArchivo) throws MyException {
//        personaFisicaService.agregarArchivo(param,idArchivo);
//    }
//
//    @DeleteMapping("/{param}")
//    public void eliminar(@PathVariable String param) throws MyException {
//        personaFisicaService.eliminar(param);
//    }
//
//}
