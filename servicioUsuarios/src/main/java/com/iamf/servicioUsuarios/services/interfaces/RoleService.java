package com.iamf.servicioUsuarios.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleService {
    Role guardar(Role role);
    void eliminar(Long id);
    List<Role> listar();
    Role getRole(Long id) throws MyException;
    Role getRole(String nombre) throws MyException;
}
