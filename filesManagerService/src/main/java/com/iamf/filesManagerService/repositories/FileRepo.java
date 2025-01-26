package com.iamf.filesManagerService.repositories;

import com.iamf.filesCommons.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

public interface FileRepo extends JpaRepository<File, String> {
    @Query(value = "SELECT a.id_archivo, a.name\n" +
            "FROM archivos a\n" +
            "JOIN econsulta_db.persona_archivos pa ON a.id_archivo = pa.archivos\n" +
            "WHERE a.name = CAST(?2 AS CHAR)\n" +
            "AND pa.persona_id = (\n" +
            "    SELECT cu.id_persona\n" +
            "    FROM econsulta_db.credenciales_de_usuario cu\n" +
            "    WHERE cu.username = CAST(?1 AS CHAR)\n" +
            "       OR cu.email = CAST(?1 AS CHAR)\n" +
            "       OR CONCAT(cu.codigo_de_llamada, cu.celular) = CAST(?1 AS CHAR)\n" +
            ");"
            , nativeQuery = true)
    List<Object[]> getFile(String param, String tipo);
}
