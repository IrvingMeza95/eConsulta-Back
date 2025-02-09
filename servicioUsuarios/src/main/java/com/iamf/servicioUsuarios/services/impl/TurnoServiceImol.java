package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Turno;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.clientes.ServicioConsultas;
import com.iamf.servicioUsuarios.repositories.TurnoRepo;
import com.iamf.servicioUsuarios.services.interfaces.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TurnoServiceImol implements TurnoService {

    @Autowired
    private TurnoRepo turnoRepo;
    @Autowired
    private ServicioConsultas servicioConsultas;

    private Turno guardar(Turno turno){
        return turnoRepo.save(turno);
    }

    private List<Turno> guardarTodo(List<Turno> turnos){
        return turnoRepo.saveAll(turnos);
    }

    @Override
    public List<Turno> crear(Turno turno) throws MyException {
        if (turno.getHorario().isEmpty() || turno.getHorario().equalsIgnoreCase(""))
            throw new MyException("Es necesario especificar el horario.");
        validar(turno.getHorario());
        List<Turno> turnos = generarSubTurnos(turno);
        return guardarTodo(turnos);
    }

    private List<Turno> generarSubTurnos(Turno turno) {
        List<Turno> turnos = new ArrayList<>();
        int limieDeCuposPorConsulta = servicioConsultas.limiteConsultasPorHorario();
        int duracionSubTurnoEnMin = 60/ limieDeCuposPorConsulta;
        String[] partes = turno.getHorario().split("-");
        for (int i = 0; i < limieDeCuposPorConsulta; i++) {
            Turno nuevoTurno = new Turno();
            nuevoTurno.setHorario(turno.getHorario());
            int minutos = i * duracionSubTurnoEnMin;
            String subHorario = String.valueOf(partes[0]) + ":";
            if (minutos == 0){
                subHorario += minutos;
                subHorario +=  minutos;
            }else{
                subHorario += minutos;
            }
            nuevoTurno.setSubHorario(subHorario);
            turnos.add(nuevoTurno);
        }
        return turnos;
    }

    private void validar(String horario) throws MyException {
        log.info("Validando si el horario " + horario + " es correcto.");
        String[] limites = horario.split("-");
        if (limites[0] == null || limites[1] == null)
            throw new MyException("El formato del horario no es valido. Ejemplo: (10-11).");
        log.info("Limite inferior: " + limites[0] + ", limite superior " + limites[1]);
        if (Long.valueOf(limites[0]) > Long.valueOf(limites[1]))
            throw new MyException("El limite inferior (" + limites[0] +
                    ") no puede ser mayor al limite superior (" + limites[1] + ").");
        if (Long.valueOf(limites[0]) < 1 || Long.valueOf(limites[0]) > 23)
            throw new MyException("El limite inferior (" + limites[0] + ") no puede ser menor que 1 o mayor a 23.");
        if (Long.valueOf(limites[1]) < 2 || Long.valueOf(limites[1]) > 24)
        throw new MyException("El limite superior (" + limites[1] + ") no puede ser menor que 2 o mayor a 24.");
        if (turnoRepo.validarExistencia(horario))
            throw new MyException("El horario " + horario + " ya  existe.");
    }

    @Override
    public Turno getTurno(String idOSubHorario) throws MyException {
        if (idOSubHorario == null)
            throw new MyException("Id o suubhorario de  turno no valida.");
        Optional<Turno> turno = null;
        try {
            Long id = Long.parseLong(idOSubHorario);
            turno = turnoRepo.findById(id);
        } catch (NumberFormatException e) {
            turno = turnoRepo.findBySubHorario(idOSubHorario);
        }
        if (turno.isEmpty())
            throw new MyException("No se encontro turno con el id o subhorarioario " + idOSubHorario + ".");
        return turno.get();
    }

    @Override
    public List<Turno> getAll() {
        return turnoRepo.findAll();
    }

    @Override
    public ResponseMessage swithcEnabled(String horario) throws MyException {
        String[] partes = horario.split("-");
        List<Turno> turnos = buscarSubHorariosAPartirDeHorario(partes[0]);
        List<Turno> turnosAct = new ArrayList<>();
        for (Turno t : turnos){
            t.setEnabled(!t.getEnabled());
            turnosAct.add(t);
        }
        String mensaje = turnosAct.get(0).getEnabled()
                ? "Los horarios dentro del turno " + partes[0] + "-" + partes[1] + " se habilitaron."
                : "Los horarios dentro del turno " + partes[0] + "-" + partes[1] + " se deshabilitaron.";
        guardarTodo(turnosAct);
        return new ResponseMessage(mensaje);
    }

    @Override
    public ResponseMessage eliminar(String horario) throws MyException {
        String[] partes = horario.split("-");
        validarExistenciaEnconsultas(partes);
        List<Turno> turnos = buscarSubHorariosAPartirDeHorario(partes[0]);
        turnoRepo.deleteAll(turnos);
        return new ResponseMessage("Los turnos dentro del horario " + horario + " fueron eliminados.");
    }

    @Override
    public List<Turno> getAllPorHorario(String horario) throws MyException {
        String[] partes = horario.split("-");
        List<Turno> turnos = turnoRepo.buscarSubHorariosAPartirDeHorario(partes[0]);
        if (turnos.isEmpty())
            throw new MyException("No se encontraron turnos con el horario " + horario + ".");
        return turnos;
    }

    private List<Turno> buscarSubHorariosAPartirDeHorario(String hora) {
        return turnoRepo.buscarSubHorariosAPartirDeHorario(hora);
    }

    private void validarExistenciaEnconsultas(String[] horario) throws MyException {
        Boolean band = null;
        try {
            band = servicioConsultas.validarExistenciaDeTurnoEnconsultas(horario[0]);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new RuntimeException("Error al validar si el turno ya ha sido usado en consultas.");
        }
        if (band)
            throw new MyException("No es posible eliminar el turno con el horario " + horario[0] + "-" + horario[1] +
                    " debido a que ya fue usado en consultas.");
    }
}
