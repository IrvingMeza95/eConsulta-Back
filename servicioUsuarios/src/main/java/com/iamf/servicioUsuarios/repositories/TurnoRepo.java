package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepo extends JpaRepository<Turno,Long> {
}
