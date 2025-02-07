package com.iamf.servicioVerificacion.clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="filesManagerService")
public interface FileManagerService {
    @GetMapping("/files/{param}/{tipo}")
    byte[] verArchivo2(@PathVariable String param, @PathVariable String tipo);
}
