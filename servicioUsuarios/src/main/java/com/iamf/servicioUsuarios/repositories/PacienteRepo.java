package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Paciente;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepo extends JpaRepository<Paciente, String> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Paciente p JOIN p.consultas c " +
            "WHERE p.id = :idPaciente AND c.pagado = true")
    Boolean tieneConsultasPagadas(@Param("idPaciente") String idPaciente);

}
