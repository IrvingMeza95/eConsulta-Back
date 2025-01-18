package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Medico;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import com.iamf.servicioUsuarios.repositories.MedicoRepo;
import com.iamf.servicioUsuarios.services.interfaces.MedicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoRepo medicoRepo;

    @Override
    public Medico guardar(Medico medico) {
        return null;
    }

    @Override
    public Medico crear(RegistroDTO registro) throws MyException {
        return null;
    }

    @Override
    public Medico getPersona(String param) throws MyException {
        return null;
    }

    @Override
    public Medico modificar(String param, PersonaDTO nuevaPersonaMoral) throws MyException {
        return null;
    }

    @Override
    public void eliminar(String param) throws MyException {

    }

    @Override
    public void agregarArchivo(String param, String idArchivo) throws MyException {

    }
}
