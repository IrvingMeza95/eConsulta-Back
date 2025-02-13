package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.responses.ResponseMessage;

import java.util.List;

public interface ConsultaService {
    Consulta crear(Consulta consulta) throws MyException;
    Consulta getConsulta(Long id) throws MyException;
    ResponseMessage eliminar(Long id) throws MyException;
    Consulta modificar(Long id, Consulta nuevaConsulta) throws MyException;
    List<Consulta> getConsultasDePersona(String email) throws MyException;
    Boolean validarExistenciaDeTurnoEnconsultas(String horario) throws MyException;
    List<Consulta> getAll();
    List<Consulta> buscarPorEmailPorRangoDeFechas(String email, String fechaInicio, String fechaFin) throws MyException;
    List<Consulta> buscarPorEmailPorRangoDeFechasFiltradoPorPagado(String email, String fechaInicio, String fechaFin, Boolean pagado) throws MyException;
    List<Consulta> buscarPorRangoDeFechas(String fechaInicio, String fechaFin) throws MyException;
    List<Consulta> buscarPorPagado(Boolean pagado) throws MyException;
    List<Consulta> buscarPorRangoDeFechasFiltradoPorPagado(String fechaInicio, String fechaFin, Boolean pagado) throws MyException;
    List<Consulta> buscarPorEmailFiltradoPorPagado(String email, Boolean pagado) throws MyException;
}
