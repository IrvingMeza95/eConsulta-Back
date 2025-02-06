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

import java.util.List;
import java.util.Optional;

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

    @Override
    public Turno crear(Turno turno) throws MyException {
        if (turno.getHorario().isEmpty() || turno.getHorario().equalsIgnoreCase(""))
            throw new MyException("Es necesario especificar el horario.");
        validar(turno.getHorario());
        return guardar(turno);
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
    public Turno getTurno(String idOHorario) throws MyException {
        if (idOHorario == null)
            throw new MyException("Id o horario de  turno no valida.");
        Optional<Turno> turno = null;
        try {
            Long id = Long.parseLong(idOHorario);
            turno = turnoRepo.findById(id);
        } catch (NumberFormatException e) {
            turno = turnoRepo.findByHorario(idOHorario);
        }
        if (turno.isEmpty())
            throw new MyException("No se encontro turno con el idOHorario " + idOHorario + ".");
        return turno.get();
    }

    @Override
    public List<Turno> getAll() {
        return turnoRepo.findAll();
    }

    @Override
    public ResponseMessage swithcEnabled(String idOHorario) throws MyException {
        Turno turno = getTurno(idOHorario);
        String mensaje = turno.getEnabled()
                ? "El turno con horario " + turno.getHorario() + " se deshabilitó."
                : "El turno con horario " + turno.getHorario() + " se habilitó.";
        turno.setEnabled(!turno.getEnabled());
        guardar(turno);
        return new ResponseMessage(mensaje);
    }

    @Override
    public ResponseMessage eliminar(String idHorario) throws MyException {
        Turno turno = getTurno(idHorario);
        validarExistenciaEnconsultas(turno.getHorario());
        turnoRepo.delete(turno);
        return new ResponseMessage("El turno con el horario " + turno.getHorario() + " fue eliminado correctamente.");
    }

    private void validarExistenciaEnconsultas(String horario) throws MyException {
        Boolean band = null;
        try {
            band = servicioConsultas.validarExistenciaDeTurnoEnconsultas(horario);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new RuntimeException("Error al validar si el turno ya ha sido usado en consultas.");
        }
        if (band)
            throw new MyException("No es posible eliminar el turno con el horario " + horario +
                    " debido a que ya fue usado en consultas.");
    }
}
