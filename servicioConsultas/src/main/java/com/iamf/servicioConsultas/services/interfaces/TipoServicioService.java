package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.TipoServicio;
import com.iamf.commons.responses.ResponseMessage;

import java.util.List;

public interface TipoServicioService {
    TipoServicio crear(TipoServicio tipoServicio) throws MyException;
    TipoServicio getTipoServicio(Long id) throws MyException;
    TipoServicio modificar(Long id, TipoServicio nuevoTipo) throws MyException;
    List<TipoServicio> getAll();
    ResponseMessage eliminar(Long id) throws MyException;
}
