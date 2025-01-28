package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepo extends JpaRepository<Consulta,String> {
}
