package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Paquete;
import com.iamf.servicioConsultas.services.interfaces.PaqueteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paquetes")
@Slf4j
public class PaqueteController {

    @Autowired
    private PaqueteService paqueteService;

    @PostMapping
    public ResponseEntity<Paquete> crear(@RequestBody List<Long> ids) throws MyException {
        return ResponseEntity.ok(paqueteService.crear(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> getPaquete(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(paqueteService.getPaquete(id));
    }

    @GetMapping
    public ResponseEntity<List<Paquete>> buscarPorServiicosIds(@RequestBody List<Long> serviciosIds) throws MyException {
        log.info("Buscando paquete con " + serviciosIds.size() + " servicios, los ids son..");
        return ResponseEntity.ok(paqueteService.buscarPaquetePorServiciosIds(serviciosIds));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> modificar(@PathVariable Long id, @RequestBody Paquete paquete) throws MyException {
        return ResponseEntity.ok(paqueteService.modificar(id,paquete));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Paquete>> getAll(){
        return ResponseEntity.ok(paqueteService.getAll());
    }

}
