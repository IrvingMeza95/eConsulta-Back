package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.enums.Roles;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Role;
import com.iamf.servicioUsuarios.repositories.RoleRepo;
import com.iamf.servicioUsuarios.services.interfaces.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public Role guardar(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public void eliminar(Long id) {
        Role role = roleRepo.findById(id).get();
        roleRepo.delete(role);
    }

    @Override
    public List<Role> listar() {
        return roleRepo.findAll();
    }

    @Override
    public Role getRole(Long id) throws MyException {
        if (id == null)
            throw new MyException("Es necesario especificar el id del rol.");
        Optional<Role> role = roleRepo.findById(id);
        if (role.isEmpty())
            throw new MyException("No se encontro el rol con id " + id + ".");
        return role.get();
    }

    @Override
    public Role getRole(String nombre) throws MyException {
        if (nombre.isEmpty())
            throw new MyException("Es necesario especificar el nombre del rol.");
        Optional<Role> role = Optional.of(roleRepo.buscarPorNombre(
                Roles.validarExistencia(nombre)
        ));
        if (role.isEmpty())
            throw new MyException("No es encontro rol con nombre " + nombre + ".");
        return role.get();
    }

}
