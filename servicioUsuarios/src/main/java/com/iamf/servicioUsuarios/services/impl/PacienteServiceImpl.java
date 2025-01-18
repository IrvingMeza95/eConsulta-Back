package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Paciente;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import com.iamf.servicioUsuarios.repositories.PacienteRepo;
import com.iamf.servicioUsuarios.services.interfaces.PacienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepo pacienteRepo;

    @Override
    public Paciente guardar(Paciente paciente) {
        return null;
    }

    @Override
    public Paciente crear(RegistroDTO registro) throws MyException {
        return null;
    }

    @Override
    public Paciente getPersona(String param) throws MyException {
        return null;
    }

    @Override
    public Paciente modificar(String param, PersonaDTO nuevaPersonaFisica) throws MyException {
        return null;
    }

    @Override
    public void eliminar(String param) throws MyException {

    }

    @Override
    public void agregarArchivo(String param, String idArchivo) throws MyException {

    }
}
