package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.enums.TipoPersona;
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
    @Value("${limite.consultas.por.horario}")
    private Integer limiteConsultasPorHorario;

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
        if (!paciente.get().getTipoPersona().equals(TipoPersona.PACIENTE))
            throw new MyException("El email " + consulta.getPaciente().getCredenciales().getEmail() +
                    " no pertenece a ningun paciente.");
        if (!paciente.get().getCredenciales().getEnabled())
            throw new MyException("El paciente seleccionado no há concluido su proceso de registro.");
        Paciente pacienteDb = new Paciente();
        pacienteDb.setId(paciente.get().getId());
        pacienteDb.setObraSocial(paciente.get().getObraSocial());
        Usuario pacienteCred = new Usuario();
        pacienteCred.setId(paciente.get().getCredenciales().getId());
        pacienteDb.setCredenciales(pacienteCred);
        consulta.setPaciente(pacienteDb);

        log.info("Buscando medico con email: " + consulta.getMedico().getCredenciales().getEmail());
        Optional<PersonaDTO> medico = Optional.of(
                servicioUsuaruis.getPersona(consulta.getMedico().getCredenciales().getEmail()));
        if (medico.isEmpty())
            throw new MyException("Error al cargar los datos del medico.");
        if (!medico.get().getTipoPersona().equals(TipoPersona.MEDICO))
            throw new MyException("El email " + consulta.getMedico().getCredenciales().getEmail() +
                    " no pertenece a ningun medico.");
        if (!medico.get().getCredenciales().getEnabled())
            throw new MyException("El médico seleccionado no há concluido su proceso de registro.");
        Medico medicoDb = new Medico();
        medicoDb.setId(medico.get().getId());
        Usuario medicoCred = new Usuario();
        medicoCred.setId(medico.get().getCredenciales().getId());
        medicoDb.setCredenciales(medicoCred);
        consulta.setMedico(medicoDb);

        log.error("Validando disponibilidad de fecha y hora.");
        String[] partes = consulta.getHorario().split(":");
        String horario = partes[0] + "-" + String.valueOf(Long.parseLong(partes[0]) + 1);
        if (consultaRepo.validarCupoDeTurno(medicoDb.getId(), horario,consulta.getFecha(),limiteConsultasPorHorario))
            throw new MyException("No hay cupo en la fecha y hora especificada.");
        if (consultaRepo.validarExistenciaDeSubHorario(medicoDb.getId(),consulta.getHorario(),consulta.getFecha()))
            throw new MyException("Ya existe una consulta programada para la misma hora y fecha y el mismo medico.");

        List<ServicioContratado> servicioContratados = servicioContratadoService.crearLista(consulta);
        consulta.setServiciosContratados(servicioContratados);

        List<Turno> turnos = servicioUsuaruis.getTurnos(null,medicoDb.getCredenciales().getEmail());
        if (turnos == null)
            throw new MyException("¡Error! puede deberse a que no se esten cargando bien los datos del medico o que el medico no tenga turnos asignados.");
        boolean band = turnos.stream()
                .anyMatch(t -> t.getEnabled() && t.getSubHorario().equalsIgnoreCase(consulta.getHorario()));
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

    @Override
    public Boolean validarExistenciaDeTurnoEnconsultas(String horario) throws MyException {
        if (horario == null)
            throw new MyException("Es necesario especificar un horario.");
        return consultaRepo.validarExistenciaDeTurnoEnconsultas(horario);
    }

    @Override
    public List<Consulta> getAll() {
        return consultaRepo.findAll();
    }

    @Override
    public List<Consulta> buscarPorEmailPorRangoDeFechas(String email, String fechaInicio, String fechaFin) throws MyException {
        PersonaDTO persona = servicioUsuaruis.getPersona(email);
        if ( fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty())
            throw new MyException("Es necesario especificar la fecha de inicio y de fin.");
        if (fechaInicio.compareTo(fechaFin) > 0)
            throw new MyException("Fecha inicio es posterior a fecha fin.");
        log.info("Buscando consultas del persona con email " + persona.getCredenciales().getEmail() +
                " en rango de fechas " + fechaInicio + " - " + fechaFin);
        return consultaRepo.buscarPorEmailYRangoDeFechas(persona.getCredenciales().getEmail(), fechaInicio,fechaFin);
    }

    @Override
    public List<Consulta> buscarPorEmailPorRangoDeFechasFiltradoPorPagado(String email, String fechaInicio, String fechaFin, Boolean pagado) throws MyException {
        PersonaDTO persona = servicioUsuaruis.getPersona(email);
        if (pagado == null)
            throw new MyException("Es necesario especificar si deseas filtrar por pagado o no pagado.");
        if ( fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty())
            throw new MyException("Es necesario especificar la fecha de inicio y de fin.");
        if (fechaInicio.compareTo(fechaFin) > 0)
            throw new MyException("Fecha inicio es posterior a fecha fin.");
        log.info("Buscando consultas del persona con email " + persona.getCredenciales().getEmail() +
                " en rango de fechas " + fechaInicio + " - " + fechaFin);
        return consultaRepo.buscarPorEmailYRangoDeFechasFiltradoPorPagado(persona.getCredenciales().getEmail(), fechaInicio,fechaFin,pagado);
    }

    @Override
    public List<Consulta> buscarPorRangoDeFechas(String fechaInicio, String fechaFin) throws MyException {
        if ( fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty())
            throw new MyException("Es necesario especificar la fecha de inicio y de fin.");
        if (fechaInicio.compareTo(fechaFin) > 0)
            throw new MyException("Fecha inicio es posterior a fecha fin.");
        log.info("Buscando consultas en rango de fechas " + fechaInicio + " - " + fechaFin);
        return consultaRepo.buscarPorRangoDeFechas(fechaInicio,fechaFin);
    }

    @Override
    public List<Consulta> buscarPorPagado(Boolean pagado) throws MyException {
        if (pagado == null)
            throw new MyException("Es necesario especificar si deseas filtrar por pagado o no pagado.");
        log.info("Buscando consultas con estatus de pago " + pagado);
        return consultaRepo.buscarPorPagadp(pagado);
    }

    @Override
    public List<Consulta> buscarPorRangoDeFechasFiltradoPorPagado(String fechaInicio, String fechaFin, Boolean pagado) throws MyException {
        if (pagado == null)
            throw new MyException("Es necesario especificar si deseas filtrar por pagado o no pagado.");
        if ( fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty())
            throw new MyException("Es necesario especificar la fecha de inicio y de fin.");
        if (fechaInicio.compareTo(fechaFin) > 0)
            throw new MyException("Fecha inicio es posterior a fecha fin.");
        log.info("Buscando consultas en rango de fechas " + fechaInicio + " - " + fechaFin + " con estatus de pago " + pagado);
        return consultaRepo.buscarPorRangoDeFechasFiltradoPorPagado(fechaInicio,fechaFin,pagado);
    }

    @Override
    public List<Consulta> buscarPorEmailFiltradoPorPagado(String email, Boolean pagado) throws MyException {
        PersonaDTO persona = servicioUsuaruis.getPersona(email);
        if (pagado == null)
            throw new MyException("Es necesario especificar si deseas filtrar por pagado o no pagado.");
        log.info("Buscando consultas del persona con email " + persona.getCredenciales().getEmail() +
                " con estatus de pago " + pagado);
        return consultaRepo.buscarPorEmailFiltradoPorPagado(persona.getCredenciales().getEmail(),pagado);
    }

}
