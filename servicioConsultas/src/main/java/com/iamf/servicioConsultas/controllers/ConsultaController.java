package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.dtos.ConsultaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.mappers.ConsultaMapper;
import com.iamf.commons.models.Consulta;
import com.iamf.servicioConsultas.services.interfaces.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;
    private final ConsultaMapper consultaMapper = new ConsultaMapper();

    @PostMapping
    public ResponseEntity<ConsultaDTO> crear(@RequestBody Consulta consulta) throws MyException {
        return ResponseEntity.ok(
                consultaMapper.getConsultaDTO((consultaService.crear(consulta)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> getConsulta(@PathVariable String id) throws MyException {
        return ResponseEntity.ok(
                consultaMapper.getConsultaDTO(consultaService.getConsulta(id))
        );
    }

}
