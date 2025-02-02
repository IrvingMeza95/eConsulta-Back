package com.iamf.filesManagerService.clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="servicioUsuarios")
public interface ServicioUsuarios {
    @PutMapping("/persona/agregar-archivo/{param}/{idArchivo}")
    void agregarArchivo(@PathVariable String param, @PathVariable String idArchivo);
}
