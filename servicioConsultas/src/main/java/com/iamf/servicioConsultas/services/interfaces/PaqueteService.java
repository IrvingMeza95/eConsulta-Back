package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Paquete;

import java.util.List;

public interface PaqueteService {
    Paquete crear(List<Long> ids) throws MyException;
    Paquete getPaquete(Long id) throws MyException;
    Paquete modificar(Long id, Paquete nuevoPaquete) throws MyException;
    List<Paquete> buscarPaquetePorServiciosIds(List<Long> serviciosIds) throws MyException;
}
