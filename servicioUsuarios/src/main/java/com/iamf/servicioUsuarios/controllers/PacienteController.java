package com.iamf.servicioUsuarios.controllers;

import com.iamf.servicioUsuarios.services.interfaces.PacienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pacientes")
@Slf4j
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/totao-pacientes")
    public ResponseEntity<Integer> totalPacientes(){
        return ResponseEntity.ok(pacienteService.totalPacientes());
    }

}
