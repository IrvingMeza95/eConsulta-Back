package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;

public interface ConsultaService {
    Consulta crear(Consulta consulta) throws MyException;
    Consulta getConsulta(String id) throws MyException;
}
