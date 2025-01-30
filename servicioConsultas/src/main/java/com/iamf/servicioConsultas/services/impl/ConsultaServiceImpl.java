package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.*;
import com.iamf.servicioConsultas.clientes.ServicioUsuaruis;
import com.iamf.servicioConsultas.repositories.ConsultaRepo;
import com.iamf.servicioConsultas.services.interfaces.ConsultaService;
import com.iamf.servicioConsultas.services.interfaces.PaqueteService;
import com.iamf.servicioConsultas.services.interfaces.ServicioMedicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ConsultaServiceImpl implements ConsultaService {

    @Autowired
    private ConsultaRepo consultaRepo;
    @Autowired
    private ServicioMedicoService servicioMedicoService;
    @Autowired
    private PaqueteService paqueteService;
    @Autowired
    private ServicioUsuaruis servicioUsuaruis;
    @Value("${porcentaje.descuento.obra.social}")
    private Double porcentajeDescuentoObraSocial;

    private Consulta guardar(Consulta consulta){
        return consultaRepo.save(consulta);
    }

    @Override
    public Consulta crear(Consulta consulta) throws MyException {
        log.info("Buscando paciente con email: " + consulta.getPaciente().getCredenciales().getEmail());
        Optional<PersonaDTO> paciente = Optional.of(
                servicioUsuaruis.getPersona(consulta.getPaciente().getCredenciales().getEmail()));
        if (paciente.isEmpty())
            throw new MyException("Error al cargar los datos del paciente.");
        Paciente pacienteDb = new Paciente();
        pacienteDb.setId(paciente.get().getId());
        Usuario pacienteCred = new Usuario();
        pacienteCred.setId(paciente.get().getCredenciales().getId());
        pacienteDb.setCredenciales(pacienteCred);
        consulta.setPaciente(pacienteDb);

        log.info("Buscando medico con email: " + consulta.getMedico().getCredenciales().getEmail());
        Optional<PersonaDTO> medico = Optional.of(
                servicioUsuaruis.getPersona(consulta.getMedico().getCredenciales().getEmail()));
        if (medico.isEmpty())
            throw new MyException("Error al cargar los datos del medico.");
        Medico medicoDb = new Medico();
        medicoDb.setId(medico.get().getId());
        Usuario medicoCred = new Usuario();
        medicoCred.setId(medico.get().getCredenciales().getId());
        medicoDb.setCredenciales(medicoCred);
        consulta.setMedico(medicoDb);

        if (consulta.getServicioMedico() == null && consulta.getPaquete() == null)
            throw new MyException("Es necesario elegir un servicio medico o un paquete.");
        if (consulta.getServicioMedico() != null){
            log.info("Buscando servicio medico con el id " + consulta.getServicioMedico().getId()
                    + " para la consulta.");
            ServicioMedico servicioMedico = servicioMedicoService.getServicioMedico(consulta.getServicioMedico().getId());
            consulta.setTotal(servicioMedico.getPrecio());
        }
        if (consulta.getPaquete() != null){
            log.info("Buscando paquete con el id " + consulta.getPaquete().getId()
                    + " para la consulta.");
            Paquete paquete = paqueteService.getPaquete(consulta.getPaquete().getId());
            consulta.setTotal(paquete.getPrecio());
        }
        if (paciente.get().getObraSocial())
            consulta.setTotal(consulta.getTotal() * (1 - porcentajeDescuentoObraSocial));
        if (consulta.getFecha().isEmpty() || consulta.getFecha().equalsIgnoreCase(""))
            throw new MyException("Es necesario seleccionar una fecha.");

        if (medico.get().getTurnos() == null)
            throw new MyException("¡Error! puede deberse a que no se esten cargando bien los datos del medico o que el medico no tenga turnos asignados.");
        Boolean band = medico.get().getTurnos().stream()
                .anyMatch(t -> t.getHorario().equalsIgnoreCase(consulta.getHorario()));
        if (!band)
            throw new MyException("El medico no tiene disponibilidad en el horario seleccionado.");

        return guardar(consulta);
    }

    @Override
    public Consulta getConsulta(String id) throws MyException {
        if (id == null)
            throw new MyException("La id no puede ser nula.");
        Optional<Consulta> consulta = consultaRepo.findById(id);
        if (consulta.isEmpty())
            throw new MyException("Noo se enccontro la consulta solicitada.");
        return consulta.get();
    }
}
