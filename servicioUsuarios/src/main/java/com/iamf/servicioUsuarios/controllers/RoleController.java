package com.iamf.servicioUsuarios.controllers;

import com.iamf.commons.models.Role;
import com.iamf.servicioUsuarios.services.interfaces.RoleService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @CircuitBreaker(name = "generic", fallbackMethod = "metodoAlternativo")
    @PostMapping
    public ResponseEntity<Role> crear(@RequestBody Role role){
        return ResponseEntity.ok(roleService.guardar(role));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        roleService.eliminar(id);
    }

    @GetMapping
    public ResponseEntity<List<Role>> listar(){
        return ResponseEntity.ok(roleService.listar());
    }

}
