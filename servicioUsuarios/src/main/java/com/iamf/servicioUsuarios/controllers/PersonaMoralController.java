//package com.iamf.servicioUsuarios.controllers;
//
//import com.iamf.commons.dtos.RequestDTO;
//import com.iamf.commons.dtos.PersonaDTO;
//import com.iamf.commons.enums.TiposDePlantillas;
//import com.iamf.commons.exceptions.MyException;
//import com.iamf.commons.mappers.PersonaMapper;
//import com.iamf.commons.models.PersonaMoral;
//import com.iamf.servicioUsuarios.clientes.ServicioVerificacion;
//import com.iamf.servicioUsuarios.dtos.RegistroDTO;
//import com.iamf.servicioUsuarios.services.interfaces.PersonaMoralService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/persona-moral")
//public class PersonaMoralController {
//
//    @Autowired
//    private PersonaMoralService personaMoralService;
//    private final PersonaMapper personaMapper = new PersonaMapper();
//    @Autowired
//    private ServicioVerificacion servicioVerificacion;
//
//    @PostMapping
//    public ResponseEntity<PersonaDTO> crear(@RequestBody RegistroDTO registro) throws MyException {
//        PersonaMoral personaMoral = personaMoralService.crear(registro);
//        RequestDTO request = new RequestDTO();
//        request.setTo(personaMoral.getCredenciales().getEmail());
//        request.setTemplate(TiposDePlantillas.CODIGO_VERIFICACION_DE_CORREO.name());
//        servicioVerificacion.codigoDeVerificacion(request);
//        return ResponseEntity.ok(personaMapper.getPersonaMoralDTO(personaMoral));
//    }
//
////    @PutMapping("/{param}")
////    public ResponseEntity<PersonaDTO> modificar(@PathVariable String param, @RequestBody PersonaMoral personaMoral) throws MyException {
////        return ResponseEntity.ok(
////                personaMapper.getPersonaMoralDTO(personaMoralService.modificar(param, personaMoral))
////        );
////    }
//
//    @PutMapping("/agregar-archivo/{param}/{idArchivo}")
//    public void agregarArchivo(@PathVariable String param, @PathVariable String idArchivo) throws MyException {
//        personaMoralService.agregarArchivo(param,idArchivo);
//    }
//
//    @DeleteMapping("/{param}")
//    public void eliminar(@PathVariable String param) throws MyException {
//        personaMoralService.eliminar(param);
//    }
//
//}
