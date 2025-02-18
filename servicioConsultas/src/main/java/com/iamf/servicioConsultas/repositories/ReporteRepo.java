package com.iamf.servicioConsultas.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReporteRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> reporteConsultasPorAnio(String anio) {
        String sql = "WITH meses AS (\n" +
                "    SELECT CONCAT(" + anio + ",'-01') AS mes UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-02') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-03') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-04') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-05') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-06') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-07') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-08') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-09') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-10') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-11') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-12')\n" +
                ")\n" +
                "SELECT \n" +
                "    m.mes,\n" +
                "    COALESCE(SUM(CASE WHEN c.pagado = 1 THEN c.total ELSE 0 END), 0) AS total_pagado,\n" +
                "    COALESCE(SUM(CASE WHEN c.pagado = 0 THEN c.total ELSE 0 END), 0) AS total_no_pagado,\n" +
                "    COALESCE(\n" +
                "        (SELECT SUM(sueldo) FROM (\n" +
                "            SELECT DISTINCT DATE_FORMAT(c.fecha, '%Y-%m') AS mes, c.fecha, c.medico_id, m.sueldo\n" +
                "            FROM econsulta_db.consultas c\n" +
                "            JOIN econsulta_db.medicos m ON c.medico_id = m.id\n" +
                "        ) AS subquery WHERE subquery.mes = m.mes)\n" +
                "    , 0) AS total_gastado\n" +
                "FROM meses m\n" +
                "LEFT JOIN econsulta_db.consultas c \n" +
                "    ON DATE_FORMAT(c.fecha, '%Y-%m') = m.mes\n" +
                "GROUP BY m.mes\n" +
                "ORDER BY m.mes;\n";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    public List<Object[]> reporteConsultasPorTipoServicioAnio(String anio) {
        String sql = "WITH meses AS (\n" +
                "    SELECT CONCAT(" + anio + ",'-01') AS mes UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-02') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-03') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-04') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-05') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-06') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-07') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-08') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-09') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-10') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-11') UNION ALL\n" +
                "    SELECT CONCAT(" + anio + ",'-12')\n" +
                ")\n" +
                "SELECT \n" +
                "    m.mes,\n" +
                "    ts.nombre AS servicio,\n" +
                "    COALESCE(SUM(CASE WHEN c.pagado = 1 THEN sc.total ELSE 0 END), 0) AS total_pagado,\n" +
                "    COALESCE(SUM(CASE WHEN c.pagado = 0 THEN sc.total ELSE 0 END), 0) AS total_no_pagado\n" +
                "FROM meses m\n" +
                "CROSS JOIN econsulta_db.tipos_de_servicios ts\n" +
                "LEFT JOIN econsulta_db.servicios_contratados sc \n" +
                "    ON ts.nombre = sc.nombre\n" +
                "LEFT JOIN econsulta_db.consultas c \n" +
                "    ON sc.consulta_id = c.id\n" +
                "    AND DATE_FORMAT(c.fecha, '%Y-%m') = m.mes\n" +
                "GROUP BY m.mes, ts.nombre\n" +
                "ORDER BY m.mes, ts.nombre;\n";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    public List<Object[]> reporteDeIngresosYEgresosPorFecha(String fechaInicio, String fechaFin) {
        String sql = "WITH MedicosPorDia AS (\n" +
                "    SELECT DISTINCT c.fecha, m.id, m.sueldo\n" +
                "    FROM econsulta_db.consultas c\n" +
                "    JOIN econsulta_db.medicos m ON c.medico_id = m.id\n" +
                "    WHERE c.fecha BETWEEN '" + fechaInicio +  "' AND '" + fechaFin + "'\n" +
                ")\n" +
                "SELECT\n" +
                "    '" + fechaInicio + "' AS FechaInicio,\n" +
                "    '" + fechaFin + "' AS FechaFin,\n" +
                "    COALESCE(SUM(CASE WHEN c.pagado = TRUE THEN c.total ELSE 0 END), 0) AS Ganancias,\n" +
                "    COALESCE(SUM(mp.sueldo), 0) AS Gastado\n" +
                "FROM econsulta_db.consultas c\n" +
                "LEFT JOIN MedicosPorDia mp ON c.medico_id = mp.id AND c.fecha = mp.fecha\n" +
                "WHERE c.fecha BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "';";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

}
