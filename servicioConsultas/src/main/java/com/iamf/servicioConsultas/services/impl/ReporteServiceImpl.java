package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.servicioConsultas.repositories.ReporteRepo;
import com.iamf.servicioConsultas.services.interfaces.ReporteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ReporteRepo reporteRepo;

    @Override
    public List<Object[]> reporteConsultasPorAnio(String anio) throws MyException {
        if (anio == null || anio.equals(""))
            throw new MyException("Es necesario especificar el año.");
        log.info("Generando reporte de resultados del año " + anio);
        List<Object[]> resultados = reporteRepo.reporteConsultasPorAnio(anio);
        return resultados;
    }

    @Override
    public List<Object[]> reporteConsultasPorTipoServicioAnio(String anio) throws MyException {
        if (anio == null || anio.equals(""))
        throw new MyException("Es necesario especificar el año.");
        log.info("Generando reporte de resultados del año " + anio + " por tipo de seervicio");
        List<Object[]> resultados = reporteRepo.reporteConsultasPorTipoServicioAnio(anio);
        return resultados;
    }
}
