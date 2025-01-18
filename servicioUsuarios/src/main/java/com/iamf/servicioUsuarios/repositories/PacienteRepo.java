package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepo extends JpaRepository<Paciente, String> {
}
