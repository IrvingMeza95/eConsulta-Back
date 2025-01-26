package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqueteRepo extends JpaRepository<Paquete, Long> {
}
