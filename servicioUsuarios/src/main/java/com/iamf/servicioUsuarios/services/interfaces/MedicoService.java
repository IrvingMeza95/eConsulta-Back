package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Medico;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import feign.Param;

import java.util.List;

public interface MedicoService {

    Medico guardar(Medico medico);

    Medico crear(RegistroDTO registro) throws MyException;

    Medico getPersona(String param) throws MyException;

    Medico modificar(String param, PersonaDTO nuevaPersonaMoral) throws MyException;

    ResponseMessage eliminar(String param) throws MyException;

    void agregarArchivo(String param, String idArchivo) throws MyException;

    List<Medico> getAll();

    List<Object[]> disponibilidadSemanal(String fecha, String email) throws MyException;

    ResponseMessage asignarRemoverTurno(String email, String horario) throws MyException;

    ResponseMessage asignarRemoverTurnATodos(String horario, String accion) throws MyException;

    List<Object[]> validarDisnibilidadDeMedicoPorFechaHorario(String fecha, String email, String horario) throws MyException;
}
