package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.models.ServicioContratado;

import java.util.List;

public interface ServicioContratadoService {
    List<ServicioContratado> crearLista(Consulta consulta) throws MyException;
    ServicioContratado getServicioContratado(Long id) throws MyException;
    List<ServicioContratado> buscarPorRangoDeFechas(String fechaInicio, String fechaFin) throws MyException;
    List<ServicioContratado> extraerServiciosContratados(List<Consulta> consultas);
}
