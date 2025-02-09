package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.ServicioContratado;
import com.iamf.servicioConsultas.services.impl.ReporteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@Slf4j
public class ReporteController {

    @Autowired
    private ReporteServiceImpl reporteService;

    @GetMapping("/reporte-por-anio/{anio}")
    public ResponseEntity<List<Object[]>> reporteConsultasPorAnio(@PathVariable String anio) throws MyException {
        return ResponseEntity.ok(reporteService.reporteConsultasPorAnio(anio));
    }

    @GetMapping("/reporte-por-anio-por-tipo/{anio}")
    public ResponseEntity<List<Object[]>> reporteConsultasPorTipoPorAnio(@PathVariable String anio) throws MyException {
        return ResponseEntity.ok(reporteService.reporteConsultasPorTipoServicioAnio(anio));
    }

    @GetMapping("/reporte-servicios-contratados-por-rango-fechas")
    public ResponseEntity<List<ServicioContratado>> reporteServiciosContratadosPorRangoDeFechas(@RequestParam String fechaInicio,
                                                                                                @RequestParam String fechaFin,
                                                                                                @RequestParam(required = false) String pacienteEmail,
                                                                                                @RequestParam(required = false) Boolean pagado) throws MyException {
        if (pacienteEmail == null || pacienteEmail.isEmpty()){
            return ResponseEntity.ok(reporteService.reporteServiciosContratadosPorRangoDeFechas(fechaInicio,fechaFin));
        }else{
            if (pagado == null){
                return ResponseEntity.ok(reporteService.reporteServiciosContratadosDePacientePorRangoDeFechas(pacienteEmail,fechaInicio,fechaFin));
            }else{
                return ResponseEntity.ok(reporteService.reporteServiciosContratadosDePacientePorRangoDeFechasFiltradoPorOagado(pacienteEmail,fechaInicio,fechaFin, pagado));
            }
        }
    }

}
