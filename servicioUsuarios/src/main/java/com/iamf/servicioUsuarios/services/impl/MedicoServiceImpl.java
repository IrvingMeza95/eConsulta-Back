package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Medico;
import com.iamf.commons.models.Turno;
import com.iamf.commons.models.Usuario;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.clientes.ServicioConsultas;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import com.iamf.servicioUsuarios.repositories.MedicoRepo;
import com.iamf.servicioUsuarios.services.interfaces.MedicoService;
import com.iamf.servicioUsuarios.services.interfaces.PersonaService;
import com.iamf.servicioUsuarios.services.interfaces.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private MedicoRepo medicoRepo;
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private ServicioConsultas servicioConsultas;

    @Override
    public Medico guardar(Medico medico) {
        return medicoRepo.save(medico);
    }

    @Override
    public Medico crear(RegistroDTO registro) throws MyException {
        Medico medico = new Medico();
        if (registro.getEspecialidad() != null || !registro.getEspecialidad().equalsIgnoreCase(""))
            medico.setEspecialidad(registro.getEspecialidad());
        if (registro.getSueldo() == null || registro.getSueldo() == 0)
            throw new MyException("El sueldo no puede ser 0 o vacio.");
        medico.setSueldo(registro.getSueldo());
        List<Turno> turnos = turnoService.getAll();
        if (turnos != null)
            medico.setTurnos(turnos);
        personaService.crear(registro, medico);
        medico = medicoRepo.save(medico);
        personaService.guardarCredencciales(medico);
        return medico;
    }

    @Override
    public Medico getPersona(String param) throws MyException {
        Usuario usuario = personaService.gatPersona(param).getCredenciales();
        Optional<Medico> medico = medicoRepo.findById(usuario.getPersona().getId());
        if (medico.isEmpty())
            throw new MyException("No se pudieron cargar los datos de la persona.");
        return medico.get();
    }

    @Override
    public Medico modificar(String param, PersonaDTO nuevoMedico) throws MyException {
        Medico medico = getPersona(param);
        if (nuevoMedico.getEspecialidad() != null)
            medico.setEspecialidad(nuevoMedico.getEspecialidad());
        if (nuevoMedico.getSueldo() != 0)
            medico.setSueldo(nuevoMedico.getSueldo());
        personaService.modificar(medico,nuevoMedico);
        return guardar(medico);
    }

    @Override
    public ResponseMessage eliminar(String param) throws MyException {
        Medico medico = getPersona(param);
        if (medicoRepo.participaEnConsultasPagadas(medico.getId()))
            throw new MyException("No es posible eliminar al medico con el email " + medico.getCredenciales().getEmail()
            + " debido a que tiene historial de pacientes.");
        medicoRepo.delete(medico);
        return new ResponseMessage("Medico con el email " + medico.getCredenciales().getEmail() + " fue eliminado correctamente.");
    }

    @Override
    public void agregarArchivo(String param, String idArchivo) throws MyException {
        Medico medico = getPersona(param);
        personaService.agregarArchivo(medico,idArchivo);
        guardar(medico);
    }

    @Override
    public List<Medico> getAll() {
        return medicoRepo.findAll();
    }

    @Override
    public List<Object[]> disponibilidadSemanal(String fecha, String email) throws MyException {
        log.info("Generando calendario de disponibilidad de horario para el medico con el email " + email);
        if (fecha == null)
            throw new MyException("Es necesario especificar una fecha.");
        if (email == null)
            throw new MyException("Es necesario especificar un email de medico");
        Medico medico = getPersona(email);
        Integer limiteConsultasPorHorario = null;
        try{
            limiteConsultasPorHorario = servicioConsultas.limiteConsultasPorHorario();
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener el limite de consultas por horario.");
        }
        List<Object[]> respuesta = medicoRepo.validarDisnibilidadDeMedicoPorSemana(fecha,medico.getId(),limiteConsultasPorHorario);
        return respuesta;
    }

    @Override
    public ResponseMessage asignarRemoverTurno(String email, String idOHorario) throws MyException {
        if (email == null)
            throw new MyException("Es neccesario un email.");
        if (idOHorario == null)
            throw new MyException("Es necesario un id o un horario de turno.");
        Medico medico = getPersona(email);
        Boolean existe = false;
        Turno turno = new Turno();
        for (Turno t : medico.getTurnos()){
            if (t.getHorario().equals(idOHorario) || String.valueOf(t.getId()).equals(idOHorario)){
                existe = true;
                turno = turnoService.getTurno(idOHorario);
                break;
            }
        }
        String mensaje = null;
        if (existe){
            log.info("Eliminando turno del medico con email " + email);
            mensaje = "Se removio el turno con horario " + turno.getHorario() +
                    " del medico con el email " + email + ".";
            medico.getTurnos().remove(turno);
        }else{
            log.info("Añadiendo turno al medico con email " + email);
            turno = turnoService.getTurno(idOHorario);
            mensaje = "Se añadio el turno con horario " + turno.getHorario() +
                    " al medico con el email " + email + ".";
            medico.getTurnos().add(turno);
        }
        guardar(medico);
        return new ResponseMessage(mensaje);
    }

    @Override
    public ResponseMessage asignarRemoverTurnATodos(String idOHorario, String accion) throws MyException {
        Turno turno = turnoService.getTurno(idOHorario);
        List<Medico> medicos = getAll();
        String mensaje = null;
        AtomicInteger contador = new AtomicInteger();
        if (accion.equalsIgnoreCase("ASIGNAR")){

            log.info("Asignando el turno con horario " + turno.getHorario() +
                    " a todos los medicos.");
            medicos.stream().map(m -> {
                if (!m.getTurnos().contains(turno)){
                    log.info("Se asigno el turno al medico con email " + m.getCredenciales().getEmail());
                    m.getTurnos().add(turno);
                    contador.getAndIncrement();
                }
                return m;
            }).collect(Collectors.toList());
            mensaje = "Se asigno el turno a " + contador + " medicos.";
        }else if (accion.equalsIgnoreCase("REMOVER")){
            log.info("Removiendo el turno con horario " + turno.getHorario() +
                    " a todos los medicos.");
            medicos.stream().map(m -> {
                if (m.getTurnos().contains(turno)){
                    log.info("Se removio el turno al medico con email " + m.getCredenciales().getEmail());
                    m.getTurnos().remove(turno);
                    contador.getAndIncrement();
                }
                return m;
            }).collect(Collectors.toList());
            mensaje = "Se removio el turno a " + contador + " medicos.";
        }else{
            throw new MyException("Es necesario especificar la accion correcta (ASIGNAR, REMOVER).");
        }
        medicoRepo.saveAll(medicos);
        return new ResponseMessage(mensaje);
    }

}
