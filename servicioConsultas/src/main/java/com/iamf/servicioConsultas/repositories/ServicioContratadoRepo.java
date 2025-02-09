package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.ServicioContratado;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioContratadoRepo extends JpaRepository<ServicioContratado, Long> {
    @Query("SELECT sc FROM ServicioContratado sc WHERE sc.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY sc.fecha ASC")
    List<ServicioContratado> buscarPorRangoDeFechas(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);
}
