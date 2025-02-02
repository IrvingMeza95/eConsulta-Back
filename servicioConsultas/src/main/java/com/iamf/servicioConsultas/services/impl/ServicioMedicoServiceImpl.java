package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.ServicioMedico;
import com.iamf.commons.models.TipoServicio;
import com.iamf.servicioConsultas.repositories.ServicioMedicoRepo;
import com.iamf.servicioConsultas.services.interfaces.ServicioMedicoService;
import com.iamf.servicioConsultas.services.interfaces.TipoServicioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServicioMedicoServiceImpl implements ServicioMedicoService {

    @Autowired
    private ServicioMedicoRepo servicioMedicoRepo;
    @Autowired
    private TipoServicioService tipoServicioService;

    private  ServicioMedico guardar(ServicioMedico servicioMedico){
        return servicioMedicoRepo.save(servicioMedico);
    }

    @Override
    public ServicioMedico crear(ServicioMedico servicioMedico) throws MyException {
        log.info("Creando nuevo servicio medico.");
        TipoServicio tipoServicio = tipoServicioService.getTipoServicio(servicioMedico.getTipoServicio().getId());
        servicioMedico.setTipoServicio(tipoServicio);
        if (servicioMedico.getDescripcion().isEmpty() || servicioMedico.getDescripcion().equalsIgnoreCase(""))
            throw new MyException("Es necesario agregar una descripcion valida al servicio..");
        if (servicioMedico.getPrecio() == null || servicioMedico.getPrecio() == 0)
            throw new MyException("Es necesario agregar un precio valido diferente a 0.");
        return guardar(servicioMedico);
    }

    @Override
    public ServicioMedico getServicioMedico(Long id) throws MyException {
        log.info("Buscando servicio con el id " + id);
        if (id == null)
            throw new MyException("Error el buscar servicio medico con el id " + id + ".");
        Optional<ServicioMedico> servicioMedico = servicioMedicoRepo.findById(id);
        if (servicioMedico.isEmpty())
            throw new MyException("No se encontro servicio medico con el id " + id + ".");
        return servicioMedico.get();
    }

    @Override
    public List<ServicioMedico> getServiciosMedicos(List<Long> ids) {
        log.info("Buscando lista de servicios.");
        List<ServicioMedico> serviciosMedicos = ids.stream().map(id -> {
                    ServicioMedico servicioMedico = null;
                    try {
                        log.info("Buscando servicio con id: " + id);
                        servicioMedico = getServicioMedico(id);
                    } catch (MyException e) {
                        log.error("Error al buscar servicio con id: " + id);
                        throw new RuntimeException(e);
                    }
                    return servicioMedico;
                })
                .filter(servicio -> servicio != null && servicio.getEnabled())
                .collect(Collectors.toList());
        return serviciosMedicos;
    }

    @Override
    public ServicioMedico modificar(Long id, ServicioMedico nuevoServicio) throws MyException {
        ServicioMedico servicioBd = getServicioMedico(id);
        if (nuevoServicio.getDescripcion() != null)
            servicioBd.setDescripcion(nuevoServicio.getDescripcion());
        if (nuevoServicio.getTipoServicio() != null) {
            if (nuevoServicio.getTipoServicio().getId() != null)
                tipoServicioService.getTipoServicio(nuevoServicio.getTipoServicio().getId());
            servicioBd.setTipoServicio(nuevoServicio.getTipoServicio());
        }
        if (nuevoServicio.getPrecio() != null)
            servicioBd.setPrecio(nuevoServicio.getPrecio());
        if (nuevoServicio.getEnabled() != null)
            servicioBd.setEnabled(nuevoServicio.getEnabled());
        return guardar(servicioBd);
    }

    @Override
    public List<ServicioMedico> getAll() {
        return servicioMedicoRepo.findAll();
    }

    @Override
    public List<ServicioMedico> getAllPorTipo(String nombre) throws MyException {
        if (nombre.isEmpty())
            throw new MyException("Es necesario especificar el tipo de servicio.");
        return servicioMedicoRepo.getAllPorTipo(nombre);
    }


}
