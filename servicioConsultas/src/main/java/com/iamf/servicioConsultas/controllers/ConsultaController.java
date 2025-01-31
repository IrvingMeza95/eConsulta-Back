package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.dtos.ConsultaDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.mappers.ConsultaMapper;
import com.iamf.commons.models.Consulta;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioConsultas.services.interfaces.ConsultaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@Slf4j
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
    public ResponseEntity<ConsultaDTO> getConsulta(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(
                consultaMapper.getConsultaDTO(consultaService.getConsulta(id))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> eliminar(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(consultaService.eliminar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> modificar(@PathVariable Long  id, @RequestBody Consulta nuevaConsulta) throws MyException {
        return ResponseEntity.ok(consultaMapper.getConsultaDTO(consultaService.modificar(id,nuevaConsulta)));
    }

    @GetMapping("/persona-consultas/{email}")
    public ResponseEntity<List<ConsultaDTO>> getConsultas(@PathVariable String email) throws MyException {
        log.info("Buscando consultas de la persona con id: " + email);
        return ResponseEntity.ok(consultaMapper.getConsultas(consultaService.getConsultasDePersona(email)));
    }

}
