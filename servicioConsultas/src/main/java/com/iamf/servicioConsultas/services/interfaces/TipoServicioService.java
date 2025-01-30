package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.TipoServicio;

public interface TipoServicioService {
    TipoServicio getTipoServicio(Long id) throws MyException;
}
