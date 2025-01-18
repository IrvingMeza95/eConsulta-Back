package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
}
