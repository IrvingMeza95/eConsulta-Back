package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Turno;
import com.iamf.commons.responses.ResponseMessage;

import java.util.List;

public interface TurnoService {
    Turno crear(Turno turno) throws MyException;
    Turno getTurno(String idOHorario) throws MyException;
    List<Turno> getAll();
    ResponseMessage swithcEnabled(String idOHorario) throws MyException;
    ResponseMessage eliminar(String idHorario) throws MyException;
}
