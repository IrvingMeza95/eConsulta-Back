package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.models.ServicioContratado;

import java.util.List;

public interface ReporteService {
    List<Object[]> reporteConsultasPorAnio(String anio) throws MyException;
    List<Object[]> reporteConsultasPorTipoServicioAnio(String anio) throws MyException;
    List<ServicioContratado> reporteServiciosContratadosPorRangoDeFechas(String fechaInicio, String fechaFin) throws MyException;
    List<ServicioContratado> reporteServiciosContratadosDePacientePorRangoDeFechas(String pacienteEmail, String fechaInicio, String fechaFin) throws MyException;
    List<ServicioContratado> reporteServiciosContratadosDePacientePorRangoDeFechasFiltradoPorOagado(String pacienteEmail, String fechaInicio, String fechaFin, Boolean pagado) throws MyException;
}
