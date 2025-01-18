package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepo extends JpaRepository<Medico, String> {
}
