package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.ServicioMedico;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioMedicoRepo extends JpaRepository<ServicioMedico, Long> {
    @Query("SELECT s FROM ServicioMedico s WHERE s.tipoServicio.nombre = :nombre")
    List<ServicioMedico> getAllPorTipo(@Param("nombre") String nombre);

}
