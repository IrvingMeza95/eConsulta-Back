package com.iamf.servicioUsuarios.services.impl;

import com.iamf.commons.models.Role;
import com.iamf.servicioUsuarios.repositories.RoleRepo;
import com.iamf.servicioUsuarios.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

}
