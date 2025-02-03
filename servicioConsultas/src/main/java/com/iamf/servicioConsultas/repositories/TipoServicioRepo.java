package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.TipoServicio;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoServicioRepo extends JpaRepository<TipoServicio, Long> {
    @Query(value = "SELECT EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM econsulta_db.servicios_contratados sc\n" +
            "    WHERE sc.nombre = CAST(?1 AS CHAR)\n" +
            ") AS coincidencia;\n", nativeQuery = true)
    List<Object[]> validarUsoDeTipoEnConsulta(@Param String nombre);
}
