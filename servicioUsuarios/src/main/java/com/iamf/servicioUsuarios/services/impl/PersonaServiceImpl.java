package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.mappers.UsuarioMapper;
import com.iamf.filesCommons.models.File;
import com.iamf.commons.models.Persona;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import com.iamf.servicioUsuarios.services.interfaces.PersonaService;
import com.iamf.servicioUsuarios.services.interfaces.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonaServiceImpl implements PersonaService    {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Persona gatPersona(String param) throws MyException {
        return usuarioService.getPersona(param);
    }

    @Override
    public void crear(RegistroDTO registro, Persona persona) throws MyException {
        if (registro.getDni() == null || registro.getDni().equalsIgnoreCase(""))
            throw new MyException("El dni no puede estar vacio.");
            persona.setDni(registro.getDni());
        if (registro.getNombre() == null || registro.getNombre().equalsIgnoreCase(""))
            throw new MyException("El nombre no puede estar vacio.");
            persona.setNombre(registro.getNombre());
        if (registro.getApellido() == null || registro.getApellido().equalsIgnoreCase(""))
            throw new MyException("El apellido no puede estar vacio.");
            persona.setApellido(registro.getApellido());
        if (registro.getFechaNacimiento() == null || registro.getFechaNacimiento().equalsIgnoreCase(""))
            throw new MyException("La fecha de nacimiento no puede estar vacio.");
            persona.setFechaNacimiento(registro.getFechaNacimiento());
        persona.setCredenciales(usuarioService.crear(registro.getCredenciales()));
        persona.setVerificado(false);
    }

    @Override
    public void guardarCredencciales(Persona persona) throws MyException {
        persona.getCredenciales().setPersona(persona);
        usuarioService.guardar(persona.getCredenciales());
    }

    @Override
    public void modificar(Persona personaActual, Persona nuevaPersona) throws MyException {
        if (nuevaPersona.getCredenciales() != null)
            personaActual.setCredenciales(usuarioService.modificar(
                personaActual.getCredenciales().getEmail(), usuarioMapper.getUsuarioDTO(
                            nuevaPersona.getCredenciales())
                    ));
//        personaActual.setVerificado(nuevaPersona.getVerificado());
        if (nuevaPersona.getTipoPersona() != null)
//        personaActual.setArchivos(nuevaPersona.getArchivos());
        if (nuevaPersona.getCiudad() != null)
            personaActual.setCiudad(nuevaPersona.getCiudad());
        if (nuevaPersona.getCodigoPostal() != null)
            personaActual.setCodigoPostal(nuevaPersona.getCodigoPostal());
        if (nuevaPersona.getDireccion() != null)
            personaActual.setDireccion(nuevaPersona.getDireccion());
        if (nuevaPersona.getNumeroExterior() != null)
            personaActual.setNumeroExterior(nuevaPersona.getNumeroExterior());
        if (nuevaPersona.getPais() != null)
            personaActual.setPais(nuevaPersona.getPais());
    }

    @Override
    public void modificar(Persona personaActual, PersonaDTO nuevaPersona) throws MyException {
        if (nuevaPersona.getCredenciales() != null)
            personaActual.setCredenciales(usuarioService.modificar(
                    personaActual.getCredenciales().getEmail(), nuevaPersona.getCredenciales()));
        if (nuevaPersona.getDni() != null)
            personaActual.setDni(nuevaPersona.getDni());
        if (nuevaPersona.getNombre() != null)
            personaActual.setNombre(nuevaPersona.getNombre());
        if (nuevaPersona.getApellido() != null)
            personaActual.setApellido(nuevaPersona.getApellido());
        if (nuevaPersona.getFechaNacimiento() != null)
            personaActual.setFechaNacimiento(nuevaPersona.getFechaNacimiento());
        if (nuevaPersona.getCiudad() != null)
            personaActual.setCiudad(nuevaPersona.getCiudad());
        if (nuevaPersona.getCodigoPostal() != null)
            personaActual.setCodigoPostal(nuevaPersona.getCodigoPostal());
        if (nuevaPersona.getDireccion() != null)
            personaActual.setDireccion(nuevaPersona.getDireccion());
        if (nuevaPersona.getNumeroExterior() != null)
            personaActual.setNumeroExterior(nuevaPersona.getNumeroExterior());
        if (nuevaPersona.getPais() != null)
            personaActual.setPais(nuevaPersona.getPais());
    }

    public List<File> getArchivos(String param) throws MyException {
//        List<File> files = usuarioService.getPersona(param).getArchivos();
        List<String> files = usuarioService.getPersona(param).getArchivos();
        if (files == null) {
            return null;
        }else{
            return files.stream().map(f -> {
                return File.builder()
                        .id(f)
                        .build();
            }).collect(Collectors.toList());
        }
    }

    @Override
    public void agregarArchivo(Persona persona, String idArchivo) {
        File file = new File();
        file.setId(idArchivo);
        persona.getArchivos().add(idArchivo);
    }

    @Override
    public TipoPersona getTipoPersona(String param) throws MyException {
        return usuarioService.getTipoPersona(param);
    }

}
