package com.iamf.servicioConsultas.services.interfaces;

import com.iamf.commons.exceptions.MyException;

import java.util.List;

public interface ReporteService {
    List<Object[]> reporteConsultasPorAnio(String anio) throws MyException;
    List<Object[]> reporteConsultasPorTipoServicioAnio(String anio) throws MyException;
}
