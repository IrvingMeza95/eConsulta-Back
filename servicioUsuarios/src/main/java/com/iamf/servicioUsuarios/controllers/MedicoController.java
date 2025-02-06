package com.iamf.servicioUsuarios.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.services.interfaces.MedicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
@Slf4j
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/disponibilidad-semanal")
    public ResponseEntity<List<Object[]>> disponibilidadSemanal(@RequestParam String email, @RequestParam String fecha) throws MyException {
        return ResponseEntity.ok(medicoService.disponibilidadSemanal(fecha,email));
    }

    @PutMapping("/asignar-remover-turno")
    public ResponseEntity<ResponseMessage> asignarRemoverTurno(@RequestParam String email, @RequestParam String idOHorarioTurno) throws MyException {
        return ResponseEntity.ok(medicoService.asignarRemoverTurno(email,idOHorarioTurno));
    }

}
