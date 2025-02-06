package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Turno;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurnoRepo extends JpaRepository<Turno,Long> {
    Optional<Turno> findByHorario(String horario);
    @Query(value = "SELECT COUNT(t) > 0 FROM Turno t WHERE t.horario = :horario")
    Boolean validarExistencia(@Param("horario") String horario);
}
