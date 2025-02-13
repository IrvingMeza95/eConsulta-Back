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
            "    -- Obtener el lunes y viernes de la semana de la fecha dada\n" +
            "    SELECT \n" +
            "        DATE_SUB(?1, INTERVAL WEEKDAY(?1) DAY) AS lunes,\n" +
            "        DATE_ADD(DATE_SUB(?1, INTERVAL WEEKDAY(?1) DAY), INTERVAL 4 DAY) AS viernes\n" +
            "),\n" +
            "turnos_medico AS (\n" +
            "    -- Obtener todos los turnos del médico con la hora extraída del campo horario\n" +
            "    SELECT \n" +
            "        t.horario, \n" +
            "        SUBSTRING_INDEX(t.horario, '-', 1) AS hora_turno\n" +
            "    FROM econsulta_db.turnos t\n" +
            "    JOIN econsulta_db.medico_turno mt ON t.id = mt.turno_id\n" +
            "    WHERE mt.medico_id = ?2 AND t.enabled = TRUE\n" +
            "),\n" +
            "seq AS (\n" +
            "    -- Generar los 5 días hábiles de la semana\n" +
            "    SELECT 0 AS num UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4\n" +
            "),\n" +
            "fechas_turnos AS (\n" +
            "    -- Generar combinaciones de fechas con los horarios del médico\n" +
            "    SELECT \n" +
            "        DATE_ADD(rf.lunes, INTERVAL s.num DAY) AS fecha, \n" +
            "        tm.horario,\n" +
            "        tm.hora_turno\n" +
            "    FROM rango_fechas rf\n" +
            "    JOIN seq s ON DATE_ADD(rf.lunes, INTERVAL s.num DAY) <= rf.viernes\n" +
            "    JOIN turnos_medico tm ON 1=1\n" +
            "),\n" +
            "consultas_medico AS (\n" +
            "    -- Contar las consultas por fecha y hora, filtrando solo las que no han sido pagadas\n" +
            "    SELECT \n" +
            "        c.fecha, \n" +
            "        SUBSTRING_INDEX(c.horario, ':', 1) AS hora_consulta,\n" +
            "        COUNT(*) AS total_consultas\n" +
            "    FROM econsulta_db.consultas c\n" +
            "    WHERE c.medico_id = ?2 AND c.pagado = FALSE\n" +
            "    GROUP BY c.fecha, hora_consulta\n" +
            "),\n" +
            "disponibilidad AS (\n" +
            "    -- Calcular disponibilidad por fecha y horario\n" +
            "    SELECT \n" +
            "        ft.fecha, \n" +
            "        ft.horario,\n" +
            "        SUM(COALESCE(cm.total_consultas, 0)) <= ?3 AS disponible\n" +
            "    FROM fechas_turnos ft\n" +
            "    LEFT JOIN consultas_medico cm \n" +
            "        ON ft.fecha = cm.fecha AND ft.hora_turno = cm.hora_consulta\n" +
            "    GROUP BY ft.fecha, ft.horario\n" +
            ")\n" +
            "-- Resultado final\n" +
            "SELECT * FROM disponibilidad\n" +
            "ORDER BY fecha, horario;\n", nativeQuery = true)
    List<Object[]> validarDisnibilidadDeMedicoPorSemana(@Param("fecha") String fecha, @Param("idMedico")
    String idMedico, @Param("limite") Integer limite);
    @Query(value = "WITH turnos_filtrados AS (\n" +
            "    SELECT t.sub_horario\n" +
            "    FROM econsulta_db.turnos t\n" +
            "    WHERE LEFT(t.sub_horario, POSITION(':' IN t.sub_horario) - 1) = SUBSTRING_INDEX(?3, '-', 1)\n" +
            "),\n" +
            "consultas_filtradas AS (\n" +
            "    SELECT c.fecha, c.horario\n" +
            "    FROM econsulta_db.consultas c\n" +
            "    WHERE c.medico_id = ?2\n" +
            "      AND c.fecha = ?1\n" +
            ")\n" +
            "SELECT \n" +
            "    ?1 AS Fecha, \n" +
            "    tf.sub_horario AS Sub_horario,\n" +
            "    CASE WHEN cf.horario IS NOT NULL THEN FALSE ELSE TRUE END AS Disponibilidad\n" +
            "FROM turnos_filtrados tf\n" +
            "LEFT JOIN consultas_filtradas cf \n" +
            "    ON tf.sub_horario = cf.horario;", nativeQuery = true)
    List<Object[]> validarDisnibilidadDeMedicoPorFechaHorario(@Param("fecha") String fecha, @Param("idMedico")
    String idMedico, @Param("horario") String horario);
    @Query(value = "SELECT \n" +
            "    ?1 AS Fecha,\n" +
            "    t.sub_horario AS Horario,\n" +
            "    CASE \n" +
            "        WHEN c.horario IS NULL THEN TRUE \n" +
            "        ELSE FALSE \n" +
            "    END AS Disponibilidad\n" +
            "FROM turnos t\n" +
            "JOIN medico_turno mt ON t.id = mt.turno_id\n" +
            "JOIN medicos m ON mt.medico_id = m.id\n" +
            "LEFT JOIN consultas c \n" +
            "    ON c.medico_id = m.id \n" +
            "    AND c.horario = t.sub_horario \n" +
            "    AND c.fecha = ?1\n" +
            "WHERE m.id = ?2\n" +
            "AND t.enabled = TRUE;\n", nativeQuery = true)
    List<Object[]> validarDisnibilidadDeMedicoPorFecha(@Param("fecha") String fecha, @Param("idMedico")
    String idMedico);
}
