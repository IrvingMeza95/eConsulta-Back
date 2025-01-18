package com.iamf.filesManagerService.clientes;

import com.iamf.commons.dtos.PersonaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="servicioUsuarios")
public interface ServicioUsuarios {
    @GetMapping("/persona/{param}")
    PersonaDTO getPersona(@PathVariable String param);
    @PutMapping("/persona-fisica/agregar-archivo/{param}/{idArchivo}")
    void agregarArchivoPersonaFisica(@PathVariable String param, @PathVariable String idArchivo);
    @PutMapping("/persona-moral/agregar-archivo/{param}/{idArchivo}")
    void agregarArchivoPersonaMoral(@PathVariable String param, @PathVariable String idArchivo);
    @PutMapping("/persona/agregar-archivo/{param}/{idArchivo}")
    void agregarArchivo(@PathVariable String param, @PathVariable String idArchivo);
}
