package com.iamf.filesManagerService.clientes;

import com.iamf.commons.dtos.ConsultaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="servicioConsultas")

public interface ServicioConsultas {
    @GetMapping("/consultas/{id}")
    ConsultaDTO getConsulta(@PathVariable Long id);
}
