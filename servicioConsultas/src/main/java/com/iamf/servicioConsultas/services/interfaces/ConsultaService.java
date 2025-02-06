package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.responses.ResponseMessage;

import java.util.List;

public interface ConsultaService {
    Consulta crear(Consulta consulta) throws MyException;
    Consulta getConsulta(Long id) throws MyException;
    ResponseMessage eliminar(Long id) throws MyException;
    Consulta modificar(Long id, Consulta nuevaConsulta) throws MyException;
    List<Consulta> getConsultasDePersona(String email) throws MyException;
    Boolean validarExistenciaDeTurnoEnconsultas(String horario) throws MyException;
}
