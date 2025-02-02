package com.iamf.filesManagerService.repositories;

import com.iamf.filesCommons.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepo extends JpaRepository<File, String> {
    @Query(value = "SELECT a.id_archivo, a.name\n" +
            "FROM econsulta_archivos_db.archivos a\n" +
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

    @Query(value = "SELECT a.id_archivo\n" +
            "FROM archivos a\n" +
            "JOIN econsulta_db.persona_archivos pa ON a.id_archivo = pa.archivos\n" +
            "WHERE pa.persona_id = (\n" +
            "    SELECT cu.id_persona\n" +
            "    FROM econsulta_db.credenciales_de_usuario cu\n" +
            "    WHERE cu.email = CAST(?1 AS CHAR)\n" +
            "       OR CONCAT(cu.codigo_de_llamada, cu.celular) = CAST(?1 AS CHAR)\n" +
            ");"
            , nativeQuery = true)
    List<Object[]> getAllFilesIdsDePersona(String param);

    @Query(value = "SELECT a.id_archivo, a.name\n" +
            "FROM econsulta_archivos_db.archivos a\n" +
            "JOIN econsulta_db.persona_archivos pa ON a.id_archivo = pa.archivos\n" +
            "WHERE a.name LIKE %?2%\n" +
            "AND pa.persona_id = (\n" +
            "    SELECT cu.id_persona\n" +
            "    FROM econsulta_db.credenciales_de_usuario cu\n" +
            "    WHERE cu.username = CAST(?1 AS CHAR)\n" +
            "       OR cu.email = CAST(?1 AS CHAR)\n" +
            "       OR CONCAT(cu.codigo_de_llamada, cu.celular) = CAST(?1 AS CHAR)\n" +
            ");"
            , nativeQuery = true)
    List<Object[]> getFilesIdsPorTipo(String param, String tipo);

}
