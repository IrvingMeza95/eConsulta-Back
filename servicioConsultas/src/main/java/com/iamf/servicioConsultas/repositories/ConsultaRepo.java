package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.Consulta;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepo extends JpaRepository<Consulta,Long> {
    @Query("SELECT c FROM Consulta c WHERE c.paciente.credenciales.email = :email OR c.medico.credenciales.email = :email")
    List<Consulta> getConsultasDePersona(@Param("email") String  email);
    @Query(value = "SELECT COUNT(c) > 0 FROM Consulta c WHERE c.horario LIKE CONCAT(:horario, '%')")
    Boolean validarExistenciaDeTurnoEnconsultas(@Param("horario") String horario);
    @Query(value = "SELECT COUNT(c) > 0 FROM Consulta c WHERE c.medico.id = :idMedico AND c.horario = :horario AND c.fecha = :fecha AND c.pagado = false")
    Boolean validarExistenciaDeSubHorario(@Param("idMedico") String idMedico, @Param("horario") String horario, @Param("fecha") String fecha);
    @Query("SELECT CASE WHEN COUNT(c) >= :limite THEN true ELSE false END FROM Consulta c WHERE c.medico.id = :idMedico AND c.horario = :horario AND c.fecha = :fecha AND c.pagado = false")
    Boolean validarCupoDeTurno(@Param("idMedico") String idMedico, @Param("horario") String horario, @Param("fecha") String fecha, @Param("limite") Integer limite);
    @Query("SELECT c FROM Consulta c WHERE c.paciente.credenciales.email = :pacinteEmail AND c.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fecha ASC")
    List<Consulta> buscarPorPacientePorRangoDeFechas(@Param("pacinteEmail") String pacinteEmail, @Param("fechaInicio") String fechaInicio,
                                                     @Param("fechaFin") String fechaFin);
    @Query("SELECT c FROM Consulta c WHERE c.paciente.credenciales.email = :pacinteEmail AND c.pagado = :pagado AND c.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fecha ASC")
    List<Consulta> buscarPorPacientePorRangoDeFechasFiltradoPorPagado(@Param("pacinteEmail") String pacinteEmail, @Param("fechaInicio") String fechaInicio,
                                                                      @Param("fechaFin") String fechaFin, @Param("pagado") Boolean pagado);

}
