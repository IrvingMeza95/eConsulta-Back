package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.models.ServicioContratado;
import com.iamf.servicioConsultas.repositories.ReporteRepo;
import com.iamf.servicioConsultas.services.interfaces.ConsultaService;
import com.iamf.servicioConsultas.services.interfaces.ReporteService;
import com.iamf.servicioConsultas.services.interfaces.ServicioContratadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private ReporteRepo reporteRepo;
    @Autowired
    private ConsultaService consultaService;
    @Autowired
    private ServicioContratadoService servicioContratadoService;

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

    @Override
    public List<ServicioContratado> reporteServiciosContratadosPorRangoDeFechas(String fechaInicio, String fechaFin) throws MyException {
        return servicioContratadoService.buscarPorRangoDeFechas(fechaInicio,fechaFin);
    }

    @Override
    public List<ServicioContratado> reporteServiciosContratadosDePacientePorRangoDeFechas(String pacienteEmail, String fechaInicio, String fechaFin) throws MyException {
        List<Consulta>  consultas = consultaService.buscarPorEmailPorRangoDeFechas(pacienteEmail,fechaInicio,fechaFin);
        return servicioContratadoService.extraerServiciosContratados(consultas);
    }

    @Override
    public List<ServicioContratado> reporteServiciosContratadosDePacientePorRangoDeFechasFiltradoPorOagado(String pacienteEmail, String fechaInicio, String fechaFin, Boolean pagado) throws MyException {
        List<Consulta>  consultas = consultaService.buscarPorEmailPorRangoDeFechasFiltradoPorPagado(pacienteEmail,fechaInicio,fechaFin, pagado);
        return servicioContratadoService.extraerServiciosContratados(consultas);
    }

}
