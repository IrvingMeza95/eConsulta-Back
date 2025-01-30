package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.ServicioMedico;

import java.util.List;

public interface ServicioMedicoService {
    ServicioMedico crear(ServicioMedico servicioMedico) throws MyException;
    ServicioMedico getServicioMedico(Long id) throws MyException;
    List<ServicioMedico> getServiciosMedicos(List<Long> ids);
    ServicioMedico modificar(Long id, ServicioMedico nuevoServicio) throws MyException;
}
