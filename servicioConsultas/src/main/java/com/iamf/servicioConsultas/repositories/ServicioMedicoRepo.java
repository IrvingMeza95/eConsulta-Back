package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.ServicioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioMedicoRepo extends JpaRepository<ServicioMedico, Long> {
}
