package com.iamf.servicioUsuarios.clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="servicioConsultas")
public interface ServicioConsultas {
    @GetMapping("/consultas/validar-existencia-de-turno/{horario}")
    Boolean validarExistenciaDeTurnoEnconsultas(@PathVariable String horario);
    @GetMapping("/consultas/limite-consultas-por-horario")
    Integer limiteConsultasPorHorario();
}
