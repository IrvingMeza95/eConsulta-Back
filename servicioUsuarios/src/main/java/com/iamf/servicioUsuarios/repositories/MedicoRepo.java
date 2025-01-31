package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Medico;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepo extends JpaRepository<Medico, String> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Medico m JOIN m.consultas c " +
            "WHERE m.id = :idMedico AND c.pagado = true")
    Boolean participaEnConsultasPagadas(@Param("idMedico") String idMedico);
}
