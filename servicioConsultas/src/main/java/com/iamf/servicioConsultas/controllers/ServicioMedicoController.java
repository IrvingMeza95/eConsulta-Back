package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.ServicioMedico;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioConsultas.services.interfaces.ServicioMedicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
@Slf4j
public class ServicioMedicoController {

    @Autowired
    private ServicioMedicoService servicioMedicoService;

    @PostMapping
    public ResponseEntity<ServicioMedico> crear(@RequestBody ServicioMedico servicioMedico) throws MyException {
        return ResponseEntity.ok(servicioMedicoService.crear(servicioMedico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioMedico> getServicioMedico(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(servicioMedicoService.getServicioMedico(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioMedico> modificar(@PathVariable Long id, @RequestBody ServicioMedico servicio) throws MyException {
        return ResponseEntity.ok(
                servicioMedicoService.modificar(id,servicio)
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ServicioMedico>> getAll(){
        return ResponseEntity.ok(servicioMedicoService.getAll());
    }

    @GetMapping("/get-all/{tipo}")
    public ResponseEntity<List<ServicioMedico>> getAllPorTipo(@PathVariable String tipo) throws MyException {
        log.info("Buscando servicios de tipo " + tipo);
        return ResponseEntity.ok(servicioMedicoService.getAllPorTipo(tipo));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> eliminar(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(servicioMedicoService.eliminar(id));
    }

}
