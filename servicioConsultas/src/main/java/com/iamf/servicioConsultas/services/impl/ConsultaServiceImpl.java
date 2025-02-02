package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.*;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioConsultas.clientes.ServicioUsuaruis;
import com.iamf.servicioConsultas.repositories.ConsultaRepo;
import com.iamf.servicioConsultas.services.interfaces.ConsultaService;
import com.iamf.servicioConsultas.services.interfaces.ServicioContratadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ConsultaServiceImpl implements ConsultaService {

    @Autowired
    private ConsultaRepo consultaRepo;
    @Autowired
    private ServicioContratadoService servicioContratadoService;
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
        if (!paciente.get().getCredenciales().getEnabled())
            throw new MyException("El paciente seleccionado no há concluido su proceso de registro.");
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
        if (!medico.get().getCredenciales().getEnabled())
            throw new MyException("El médico seleccionado no há concluido su proceso de registro.");
        Medico medicoDb = new Medico();
        medicoDb.setId(medico.get().getId());
        Usuario medicoCred = new Usuario();
        medicoCred.setId(medico.get().getCredenciales().getId());
        medicoDb.setCredenciales(medicoCred);
        consulta.setMedico(medicoDb);

        List<ServicioContratado> servicioContratados = servicioContratadoService.crearLista(consulta);
        consulta.setServiciosContratados(servicioContratados);

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
    public Consulta getConsulta(Long id) throws MyException {
        if (id == null)
            throw new MyException("La id no puede ser nula.");
        Optional<Consulta> consulta = consultaRepo.findById(id);
        if (consulta.isEmpty())
            throw new MyException("No se enccontro la consulta solicitada.");
        return consulta.get();
    }

    @Override
    public ResponseMessage eliminar(Long id) throws MyException {
        Consulta consulta = getConsulta(id);
        if (consulta.getPagado()){
            throw new MyException("No es posible eliminar una consulta que ya há sido pagada.");
        }else{
            consultaRepo.delete(consulta);
            return new ResponseMessage("Consulta con el id " + id + " há sido eliminada correctamente.");
        }
    }

    @Override
    public Consulta modificar(Long id, Consulta nuevaConsulta) throws MyException {
        log.info("Modificando consulta con id " + id);
        Consulta consulta = getConsulta(id);
        if (nuevaConsulta.getPagado() != null)
            consulta.setPagado(nuevaConsulta.getPagado());
        return guardar(consulta);
    }

    @Override
    public List<Consulta> getConsultasDePersona(String email) throws MyException {
        if (email.isEmpty())
            throw new MyException("Es necesario un id de alguna persona.");
        return consultaRepo.getConsultasDePersona(email);
    }

}
