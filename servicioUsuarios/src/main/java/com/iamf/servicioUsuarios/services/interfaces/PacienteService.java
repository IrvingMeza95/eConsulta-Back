package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Paciente;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;

import java.util.List;

public interface PacienteService {

    Paciente guardar(Paciente paciente);
    Paciente crear(RegistroDTO registro) throws MyException;
    Paciente getPersona(String param) throws MyException;
    Paciente modificar(String param, PersonaDTO nuevoPaciente) throws MyException;
    ResponseMessage eliminar(String param) throws MyException;
    void agregarArchivo(String param, String idArchivo) throws MyException;
    List<Paciente> getAll();
}
