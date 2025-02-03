package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.TipoServicio;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioConsultas.repositories.TipoServicioRepo;
import com.iamf.servicioConsultas.services.interfaces.TipoServicioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TipoServicioServiceImpl implements TipoServicioService {

    @Autowired
    private TipoServicioRepo tipoServicioRepo;

    private TipoServicio guardar(TipoServicio tipoServicio){
        return tipoServicioRepo.save(tipoServicio);
    }

    @Override
    public TipoServicio crear(TipoServicio tipoServicio) throws MyException {
        if (tipoServicio.getNombre() == null || tipoServicio.getNombre().equals(""))
            throw new MyException("Es necesario un nombre.");
        return guardar(tipoServicio);
    }

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

    @Override
    public TipoServicio modificar(Long id, TipoServicio nuevoTipo) throws MyException {
        if (id == null)
            throw new MyException("Es necesario un id del tipo servicio.");
        TipoServicio tipoBd = getTipoServicio(id);
        validarUsoEnConsultas(tipoBd.getNombre());
        if (nuevoTipo.getNombre() != null)
            tipoBd.setNombre(nuevoTipo.getNombre());
        return guardar(tipoBd);
    }

    private void validarUsoEnConsultas(String nombre) throws MyException {
        List<Object[]> haSidoUsadoEnConsulta = tipoServicioRepo.validarUsoDeTipoEnConsulta(nombre);
        if (String.valueOf(haSidoUsadoEnConsulta.get(0)[0]).equals("1"))
            throw new MyException("No es posible modificar o eliminar el tipo de servicio ya que ya ha sido usado en consultas.");
    }

    @Override
    public List<TipoServicio> getAll() {
        return tipoServicioRepo.findAll();
    }

    @Override
    public ResponseMessage eliminar(Long id) throws MyException {
        TipoServicio tipo = getTipoServicio(id);
        validarUsoEnConsultas(tipo.getNombre());
        tipoServicioRepo.delete(tipo);
        return new ResponseMessage("El tipo de servicio " + tipo.getNombre() + " fue eliminado correctamente.");
    }
}
