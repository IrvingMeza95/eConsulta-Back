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
    @Query(value = "SELECT EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM econsulta_db.consultas c\n" +
            "    JOIN econsulta_db.paquete_servicio ps ON c.id_paquete = ps.paquete_id\n" +
            "    WHERE ps.servicio_medico_id = ?1\n" +
            ") AS existe;", nativeQuery = true)
    List<Object[]> existeEnConsuñtas(Long id);
}
