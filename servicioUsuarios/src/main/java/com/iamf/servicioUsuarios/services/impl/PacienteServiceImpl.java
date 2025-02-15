package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Paciente;
import com.iamf.commons.models.Usuario;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import com.iamf.servicioUsuarios.repositories.PacienteRepo;
import com.iamf.servicioUsuarios.services.interfaces.PacienteService;
import com.iamf.servicioUsuarios.services.interfaces.PersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private PacienteRepo pacienteRepo;

    @Override
    public Paciente guardar(Paciente paciente) {
        return pacienteRepo.save(paciente);
    }

    @Override
    public Paciente crear(RegistroDTO registro) throws MyException {
        Paciente paciente = new Paciente();
        personaService.crear(registro, paciente);
        paciente.setObraSocial(registro.getObraSocial());
        paciente = pacienteRepo.save(paciente);
        personaService.guardarCredencciales(paciente);
        return paciente;
    }

    @Override
    public Paciente getPersona(String param) throws MyException {
        Usuario usuario = personaService.gatPersona(param).getCredenciales();
        Optional<Paciente> paciente = pacienteRepo.findById(usuario.getPersona().getId());
        if (paciente.isEmpty())
            throw new MyException("No se pudieron cargar los datos de la persona.");
        return paciente.get();
    }

    @Override
    public Paciente modificar(String param, PersonaDTO nuevoPaciente) throws MyException {
        Paciente paciente = getPersona(param);
        if (nuevoPaciente.getObraSocial() != null)
            paciente.setObraSocial(nuevoPaciente.getObraSocial());
        personaService.modificar(paciente,nuevoPaciente);
        return guardar(paciente);
    }

    @Override
    public ResponseMessage eliminar(String param) throws MyException {
        Paciente paciente = getPersona(param);
        if (pacienteRepo.tieneConsultasPagadas(paciente.getId()))
            throw new MyException("No es posible eliminar al paciente con el email " + paciente.getCredenciales().getEmail()
            + " porque cuenta con historial de consultas pagadas.");
        pacienteRepo.delete(paciente);
        return new ResponseMessage("El paciente con el email " + paciente.getCredenciales().getEmail() +
                " fue eliminado correctamente.");
    }

    @Override
    public void agregarArchivo(String param, String idArchivo) throws MyException {
        Paciente paciente = getPersona(param);
        personaService.agregarArchivo(paciente,idArchivo);
        guardar(paciente);
    }

    @Override
    public List<Paciente> getAll() {
        return pacienteRepo.findAll();
    }

    @Override
    public Integer totalPacientes() {
        return pacienteRepo.totalPacientes();
    }

}
