//package com.iamf.servicioUsuarios.services.impl;
//
//import com.iamf.commons.dtos.PersonaDTO;
//import com.iamf.commons.enums.TipoPersona;
//import com.iamf.commons.exceptions.MyException;
//import com.iamf.commons.models.Persona;
//import com.iamf.commons.models.PersonaFisica;
//import com.iamf.commons.models.Usuario;
//import com.iamf.commons.responses.ResponseMessage;
//import com.iamf.servicioUsuarios.dtos.RegistroDTO;
//import com.iamf.servicioUsuarios.repositories.PersonaFisicaRepo;
//import com.iamf.servicioUsuarios.services.interfaces.PersonaFisicaService;
//import com.iamf.servicioUsuarios.services.interfaces.PersonaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class PersonaFisicaImpl implements PersonaFisicaService {
//
//    @Autowired
//    private PersonaFisicaRepo personaFisicaRepo;
//    @Autowired
//    private PersonaService personaService;
//
//    @Override
//    public PersonaFisica guardar(PersonaFisica personaFisica) {
//        return personaFisicaRepo.save(personaFisica);
//    }
//
//    @Override
//    public PersonaFisica crear(RegistroDTO registro) throws MyException {
//        PersonaFisica persona = new PersonaFisica();
////        persona.setTipoPersona(TipoPersona.FISICA);
//        personaService.crear(registro, persona);
//        persona = personaFisicaRepo.save(persona);
//        personaService.guardarCredencciales(persona);
//        return persona;
//    }
//
//    @Override
//    public PersonaFisica getPersona(String param) throws MyException {
//        Usuario usuario = personaService.gatPersona(param).getCredenciales();
//        Optional<PersonaFisica> personaFisica = personaFisicaRepo.findById(usuario.getPersona().getId());
//        if (personaFisica.isEmpty())
//            throw new MyException("No se pudieron cargar los datos de la persona.");
//        return personaFisica.get();
//    }
//
//    @Override
//    public PersonaFisica modificar(String param, PersonaDTO nuevaPersonaFisica) throws MyException {
//        PersonaFisica personaFisica = getPersona(param);
//        if (nuevaPersonaFisica.getIne() != null)
//            personaFisica.setIne(nuevaPersonaFisica.getIne());
//        if (nuevaPersonaFisica.getNombres() != null)
//            personaFisica.setNombres(nuevaPersonaFisica.getNombres());
//        if (nuevaPersonaFisica.getApellidos() != null)
//            personaFisica.setApellidos(nuevaPersonaFisica.getApellidos());
//        if (nuevaPersonaFisica.getFechaNacimiento() != null)
//            personaFisica.setFechaNacimiento(nuevaPersonaFisica.getFechaNacimiento());
//        personaService.modificar(personaFisica,nuevaPersonaFisica);
//        return guardar(personaFisica);
//    }
//
//    @Override
//    public void eliminar(String param) throws MyException {
//        PersonaFisica personaFisica = getPersona(param);
//        personaFisicaRepo.delete(personaFisica);
//    }
//
//    @Override
//    public void agregarArchivo(String param, String idArchivo) throws MyException {
//        PersonaFisica personaFisica = getPersona(param);
//        personaService.agregarArchivo(personaFisica,idArchivo);
//        guardar(personaFisica);
//    }
//
//}
