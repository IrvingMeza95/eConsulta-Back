package com.iamf.servicioUsuarios.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Turno;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.services.interfaces.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
@Slf4j
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    @PostMapping
    public ResponseEntity<Turno> crear(@RequestBody Turno turno) throws MyException {
        return ResponseEntity.ok(turnoService.crear(turno));
    }

    @GetMapping("/{idOHorario}")
    public ResponseEntity<Turno> getTurno(@PathVariable String idOHorario) throws MyException {
        return ResponseEntity.ok(turnoService.getTurno(idOHorario));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Turno>> getAll(){
        return ResponseEntity.ok(turnoService.getAll());
    }

    @PutMapping("/habilitar-deshabilitar/{idHorario}")
    public ResponseEntity<ResponseMessage> habilitarDeshabilitar(@PathVariable String idHorario) throws MyException {
        return ResponseEntity.ok(turnoService.swithcEnabled(idHorario));
    }

    @DeleteMapping("/{idHorario}")
    public ResponseEntity<ResponseMessage> eliminar(@PathVariable String idHorario) throws MyException {
        return ResponseEntity.ok(turnoService.eliminar(idHorario));
    }

}
