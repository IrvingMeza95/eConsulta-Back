package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoServicioRepo extends JpaRepository<TipoServicio, Integer> {
}
