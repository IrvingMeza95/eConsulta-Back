package com.iamf.servicioConsultas.controllers;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.TipoServicio;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioConsultas.services.interfaces.TipoServicioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-de-servicios")
@Slf4j
public class TipoServicioController {

    @Autowired
    private TipoServicioService tipoServicioService;

    @PostMapping
    public ResponseEntity<TipoServicio> crear(@RequestBody TipoServicio tipoServicio) throws MyException {
        return ResponseEntity.ok(tipoServicioService.crear(tipoServicio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicio> getTipo(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(tipoServicioService.getTipoServicio(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<TipoServicio>> getAll(){
        return ResponseEntity.ok(tipoServicioService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoServicio> modificar(@PathVariable Long id, @RequestBody TipoServicio nuevoTipo) throws MyException {
        return ResponseEntity.ok(tipoServicioService.modificar(id,nuevoTipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> eliminar(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok(tipoServicioService.eliminar(id));
    }

}
