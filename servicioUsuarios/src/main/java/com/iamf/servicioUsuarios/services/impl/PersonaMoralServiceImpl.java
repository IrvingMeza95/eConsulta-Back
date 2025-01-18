//package com.iamf.servicioUsuarios.services.impl;
//
//import com.iamf.commons.dtos.PersonaDTO;
//import com.iamf.commons.enums.TipoPersona;
//import com.iamf.commons.exceptions.MyException;
//import com.iamf.commons.models.PersonaFisica;
//import com.iamf.commons.models.PersonaMoral;
//import com.iamf.commons.models.Usuario;
//import com.iamf.commons.responses.ResponseMessage;
//import com.iamf.servicioUsuarios.dtos.RegistroDTO;
//import com.iamf.servicioUsuarios.repositories.PersonaMoralRepo;
//import com.iamf.servicioUsuarios.services.interfaces.PersonaMoralService;
//import com.iamf.servicioUsuarios.services.interfaces.PersonaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class PersonaMoralServiceImpl implements PersonaMoralService {
//
//    @Autowired
//    private PersonaMoralRepo personaMoralRepo;
//    @Autowired
//    private PersonaService personaService;
//
//    @Override
//    public PersonaMoral guardar(PersonaMoral personaMoral) {
//        return personaMoralRepo.save(personaMoral);
//    }
//
//    @Override
//    public PersonaMoral crear(RegistroDTO registro) throws MyException {
//        PersonaMoral persona = new PersonaMoral();
//        personaService.crear(registro, persona);
//        persona = personaMoralRepo.save(persona);
//        personaService.guardarCredencciales(persona);
//        return persona;
//    }
//
//    @Override
//    public PersonaMoral getPersona(String param) throws MyException {
//        Usuario usuario = personaService.gatPersona(param).getCredenciales();
//        Optional<PersonaMoral> personaMoral = personaMoralRepo.findById(usuario.getPersona().getId());
//        if (personaMoral.isEmpty())
//            throw new MyException("No se pudieron cargar los datos de la persona.");
//        return personaMoral.get();
//    }
//
//    @Override
//    public PersonaMoral modificar(String param, PersonaDTO nuevaPersonaMoral) throws MyException {
//        PersonaMoral personaMoral = getPersona(param);
//        if (nuevaPersonaMoral.getRfc() != null)
//            personaMoral.setRfc(nuevaPersonaMoral.getRfc());
//        if (nuevaPersonaMoral.getNombre() != null)
//            personaMoral.setNombre(nuevaPersonaMoral.getNombre());
//        if (nuevaPersonaMoral.getRazonSocial() != null)
//            personaMoral.setRazonSocial(nuevaPersonaMoral.getRazonSocial());
//        personaService.modificar(personaMoral,nuevaPersonaMoral);
//        return guardar(personaMoral);
//    }
//
//    @Override
//    public void eliminar(String param) throws MyException {
//        PersonaMoral personaMoral = getPersona(param);
//        personaMoralRepo.delete(personaMoral);
//    }
//
//    @Override
//    public void agregarArchivo(String param, String idArchivo) throws MyException {
//        PersonaMoral personaMoral = getPersona(param);
//        personaService.agregarArchivo(personaMoral,idArchivo);
//        guardar(personaMoral);
//    }
//
//}
