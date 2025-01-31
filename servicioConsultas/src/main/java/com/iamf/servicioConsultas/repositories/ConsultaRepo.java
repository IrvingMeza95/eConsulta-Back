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
}
