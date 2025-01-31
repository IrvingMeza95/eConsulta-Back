package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.models.Paquete;
import com.iamf.commons.models.ServicioContratado;
import com.iamf.commons.models.ServicioMedico;
import com.iamf.servicioConsultas.repositories.ServicioContratadoRepo;
import com.iamf.servicioConsultas.services.interfaces.PaqueteService;
import com.iamf.servicioConsultas.services.interfaces.ServicioContratadoService;
import com.iamf.servicioConsultas.services.interfaces.ServicioMedicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServicioContratadoServiceImpl implements ServicioContratadoService {

    @Autowired
    private ServicioContratadoRepo servicioContratadoRepo;
    @Autowired
    private ServicioMedicoService servicioMedicoService;
    @Autowired
    private PaqueteService paqueteService;

    private ServicioContratado guardar(ServicioContratado servicioContratado){
        return servicioContratadoRepo.save(servicioContratado);
    }

    @Override
    public List<ServicioContratado> crearLista(Consulta consulta) throws MyException {
        log.info("Creando lista de servicios contratados.");
        List<ServicioMedico> servicios = new ArrayList<>();
        if (consulta.getIdServicioMedico() != null){
            log.info("Cargando datos de un solo servicio.");
            ServicioMedico servicioBd = servicioMedicoService.getServicioMedico(consulta.getIdServicioMedico());
            consulta.setTotal(servicioBd.getPrecio());
            servicios.add(servicioBd);
        }else if (consulta.getIdPaquete() != null){
            log.info("Cargando datos de servicios de un paquete.");
            Paquete paquete = paqueteService.getPaquete(consulta.getIdPaquete());
            consulta.setTotal(paquete.getPrecio());
            servicios.addAll(paquete.getServicios());
        }else{
            log.error("No se asocio ningun id de servicio o paquete.");
            throw new MyException("Es necesario asociar o un servicio o un paquete a una consulta.");
        }

        List<ServicioContratado> servicioContratados = servicios.stream().map(s -> {
            log.info("Registando datos de servicio con el id " + s.getId());

            ServicioContratado nuevoServicio = ServicioContratado.builder()
                    .nombre(s.getTipoServicio().getNombre())
                    .descripcion(s.getDescripcion())
                    .precio(s.getPrecio())
                    .build();
            return nuevoServicio;
        }).collect(Collectors.toList());

        return servicioContratados;
    }

    @Override
    public ServicioContratado getServicioContratado(Long id) throws MyException {
        if (id == null)
            throw new MyException("El id no puede ser vacio.");
        Optional<ServicioContratado> servicioContratado = servicioContratadoRepo.findById(id);
        if (servicioContratado.isEmpty())
            throw new MyException("No se encontro el servicio contratado  con el id " + id + ".");
        return servicioContratado.get();
    }
}
