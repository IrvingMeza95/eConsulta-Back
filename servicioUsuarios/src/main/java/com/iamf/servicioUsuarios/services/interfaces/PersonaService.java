package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Persona;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;

public interface PersonaService {
    Persona gatPersona(String param) throws MyException;
    void crear(RegistroDTO registro, Persona persona) throws MyException;
    void guardarCredencciales(Persona persona) throws MyException;
    void modificar(Persona personaActual, Persona nuevaPersona) throws MyException;
    void modificar(Persona personaActual, PersonaDTO nuevaPersona) throws MyException;
    void agregarArchivo(Persona persona, String idArchivo);
    TipoPersona getTipoPersona(String param) throws MyException;
}
