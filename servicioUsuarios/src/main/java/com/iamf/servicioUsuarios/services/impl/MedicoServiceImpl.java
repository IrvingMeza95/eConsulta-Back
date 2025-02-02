package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Medico;
import com.iamf.commons.models.Paciente;
import com.iamf.commons.models.Turno;
import com.iamf.commons.models.Usuario;
import com.iamf.commons.responses.ResponseMessage;
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

@Service
@Slf4j
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private MedicoRepo medicoRepo;
    @Autowired
    private TurnoService turnoService;

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
}
