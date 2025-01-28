package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Turno;

import java.util.List;

public interface TurnoService {
    Turno crear(Turno turno) throws MyException;
    Turno getTurno(Long id) throws MyException;
    List<Turno> getAll();
    void swithcEnabled(Long id) throws MyException;
}
