package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Turno;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepo extends JpaRepository<Turno,Long> {
    @Query("SELECT t FROM Turno t WHERE t.subHorario = :subHorario")
    Optional<Turno> findBySubHorario(@Param("subHorario") String subHorario);
    @Query(value = "SELECT COUNT(t) > 0 FROM Turno t WHERE t.horario = :horario")
    Boolean validarExistencia(@Param("horario") String horario);
    @Query("SELECT t FROM Turno t WHERE t.subHorario LIKE CONCAT(:subHorario, '%')")
    List<Turno> buscarSubHorariosAPartirDeHorario(@Param("subHorario") String subHorario);
    @Query("SELECT m.turnos FROM Medico m WHERE m.credenciales.email = :medicoEmail")
    List<Turno> getAllPorMedico(@Param("medicoEmail") String medicoEmail);
}
