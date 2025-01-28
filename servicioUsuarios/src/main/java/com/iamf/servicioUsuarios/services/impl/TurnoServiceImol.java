package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Turno;
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

    private Turno guardar(Turno turno){
        return turnoRepo.save(turno);
    }

    @Override
    public Turno crear(Turno turno) throws MyException {
        if (turno.getHorario().isEmpty() || turno.getHorario().equalsIgnoreCase(""))
            throw new MyException("Es necesario especificar el horario.");
        return guardar(turno);
    }

    @Override
    public Turno getTurno(Long id) throws MyException {
        if (log == null)
            throw new MyException("Id de  turno no valida.");
        Optional<Turno> turno = turnoRepo.findById(id);
        if (turno.isEmpty())
            throw new MyException("No se encontro turno con el id " + id + ".");
        return turno.get();
    }

    @Override
    public List<Turno> getAll() {
        return turnoRepo.findAll();
    }

    @Override
    public void swithcEnabled(Long id) throws MyException {
        Turno turno = getTurno(id);
        turno.setEnabled(!turno.getEnabled());
        guardar(turno);
    }
}
