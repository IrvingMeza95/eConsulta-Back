package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.servicioConsultas.services.impl.ReporteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
