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
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${porcentaje.descuento.obra.social}")
    private Double porcentajeDescuentoObraSocial;
    @Value("${porcentaje.descuento.paquete}")
    private Double porcentajeDescuentoPaquete;

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
            if (consulta.getPaciente().getObraSocial()){
                consulta.setTotal(servicioBd.getPrecio() * (1 - porcentajeDescuentoObraSocial));
            }else{
                consulta.setTotal(servicioBd.getPrecio());
            }
            servicios.add(servicioBd);
        }else if (consulta.getIdPaquete() != null){
            log.info("Cargando datos de servicios de un paquete.");
            Paquete paquete = paqueteService.getPaquete(consulta.getIdPaquete());
            Double total = null;
            if (consulta.getPaciente().getObraSocial()){
                total = paquete.getPrecio() / (1 - porcentajeDescuentoPaquete);
                consulta.setTotal(total * (1 - (porcentajeDescuentoPaquete + porcentajeDescuentoObraSocial)));
            }else{
                consulta.setTotal(paquete.getPrecio());
            }
            servicios.addAll(paquete.getServicios());
        }else{
            log.error("No se asocio ningun id de servicio o paquete.");
            throw new MyException("Es necesario asociar o un servicio o un paquete a una consulta.");
        }

        List<ServicioContratado> servicioContratados = servicios.stream().map(s -> {
            log.info("Registando datos de servicio con el id " + s.getId());
            Double pdos = consulta.getPaciente().getObraSocial() ? porcentajeDescuentoObraSocial : 0;
            Double pdp = (consulta.getIdPaquete() == null) ? 0 : porcentajeDescuentoPaquete;
            log.info("Total descuento: " + (pdos + pdp));
            ServicioContratado nuevoServicio = ServicioContratado.builder()
                    .fecha(consulta.getFecha())
                    .nombre(s.getTipoServicio().getNombre())
                    .descripcion(s.getDescripcion())
                    .precio(s.getPrecio())
                    .porcentajeDescuentoObraSocial(pdos)
                    .porcentajeDescuentoPaquete(pdp)
                    .total(s.getPrecio() * (1 - (pdos + pdp)))
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

    @Override
    public List<ServicioContratado> buscarPorRangoDeFechas(String fechaInicio, String fechaFin) throws MyException {
        if ( fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty())
            throw new MyException("Es necesario especificar la fecha de inicio y de fin.");
        if (fechaInicio.compareTo(fechaFin) > 0)
            throw new MyException("Fecha inicio es posterior a fecha fin.");
        log.info("Buscando consultas en rango de fechas " + fechaInicio + " - " + fechaFin);
        return servicioContratadoRepo.buscarPorRangoDeFechas(fechaInicio,fechaFin);
    }

    @Override
    public List<ServicioContratado> extraerServiciosContratados(List<Consulta> consultas) {
        List<ServicioContratado> servicioContratados = new ArrayList<>();
        for (Consulta c : consultas){
            servicioContratados.addAll(c.getServiciosContratados());
        }
        return servicioContratados;
    }

}
