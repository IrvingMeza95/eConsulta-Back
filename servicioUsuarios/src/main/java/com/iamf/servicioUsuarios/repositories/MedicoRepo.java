package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Medico;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoRepo extends JpaRepository<Medico, String> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Medico m JOIN m.consultas c " +
            "WHERE m.id = :idMedico AND c.pagado = true")
    Boolean participaEnConsultasPagadas(@Param("idMedico") String idMedico);
    @Query(value = "WITH rango_fechas AS (\n" +
            "    -- Generar el rango de fechas de lunes a viernes basado en la fecha proporcionada\n" +
            "    SELECT \n" +
            "        DATE_SUB(?1, INTERVAL WEEKDAY(?1) DAY) AS lunes,\n" +
            "        DATE_ADD(DATE_SUB(?1, INTERVAL WEEKDAY(?1) DAY), INTERVAL 4 DAY) AS viernes\n" +
            "),\n" +
            "turnos_medico AS (\n" +
            "    -- Obtener todos los turnos del médico especificado\n" +
            "    SELECT t.horario \n" +
            "    FROM econsulta_db.turnos t\n" +
            "    JOIN econsulta_db.medico_turno mt ON t.id = mt.turno_id\n" +
            "    WHERE mt.medico_id = ?2 AND t.enabled = TRUE\n" +
            "),\n" +
            "fechas_turnos AS (\n" +
            "    -- Generar todas las combinaciones de fechas del rango con los turnos del médico\n" +
            "    SELECT rf.lunes + INTERVAL seq DAY AS fecha, tm.horario\n" +
            "    FROM (SELECT lunes, viernes FROM rango_fechas) rf\n" +
            "    JOIN (SELECT 0 AS seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) s\n" +
            "    JOIN turnos_medico tm ON 1=1\n" +
            "    WHERE rf.lunes + INTERVAL seq DAY BETWEEN rf.lunes AND rf.viernes\n" +
            "),\n" +
            "consultas_medico AS (\n" +
            "    -- Contar las consultas por fecha y horario del médico\n" +
            "    SELECT c.fecha, c.horario, COUNT(*) AS total_consultas\n" +
            "    FROM econsulta_db.consultas c\n" +
            "    WHERE c.medico_id = ?2\n" +
            "    GROUP BY c.fecha, c.horario\n" +
            ")\n" +
            "-- Generar la tabla final con la validación de disponibilidad\n" +
            "SELECT \n" +
            "    ft.fecha, \n" +
            "    ft.horario, \n" +
            "    IF(COALESCE(cm.total_consultas, 0) < ?3, TRUE, FALSE) AS disponible\n" +
            "FROM fechas_turnos ft\n" +
            "LEFT JOIN consultas_medico cm \n" +
            "    ON ft.fecha = cm.fecha AND ft.horario = cm.horario\n" +
            "ORDER BY ft.fecha, ft.horario;", nativeQuery = true)
    List<Object[]> validarDisnibilidadDeMedicoPorSemana(@Param("fecha") String fecha, @Param("idMedico")
    String idMedico, @Param("limite") Integer limite);

}
