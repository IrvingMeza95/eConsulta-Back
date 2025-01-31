package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.ServicioContratado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioContratadoRepo extends JpaRepository<ServicioContratado, Long> {
}
