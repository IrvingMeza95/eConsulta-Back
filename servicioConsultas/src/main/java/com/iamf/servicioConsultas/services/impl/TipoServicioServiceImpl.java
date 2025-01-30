package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.TipoServicio;
import com.iamf.servicioConsultas.repositories.TipoServicioRepo;
import com.iamf.servicioConsultas.services.interfaces.TipoServicioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TipoServicioServiceImpl implements TipoServicioService {

    @Autowired
    private TipoServicioRepo tipoServicioRepo;

    @Override
    public TipoServicio getTipoServicio(Long id) throws MyException {
        log.info("Buscando tipo de servicio con el id " + id);
        if (id == null)
            throw new MyException("La id no puede ser nula.");
        Optional<TipoServicio> tipoServicio = tipoServicioRepo.findById(id);
        if (tipoServicio.isEmpty())
            throw new MyException("El tipo de servicio con el id " + id + " no existe.");
        return tipoServicio.get();
    }
}
