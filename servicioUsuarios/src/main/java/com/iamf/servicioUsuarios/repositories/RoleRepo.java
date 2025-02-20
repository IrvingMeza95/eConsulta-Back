package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.enums.Roles;
import com.iamf.commons.models.Role;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepo extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.nombre =:nombre")
    Role buscarPorNombre(@Param("nombre") Roles nombre);
}
