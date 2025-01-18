package com.iamf.servicioOauth.services;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.models.Usuario;

public interface IUsuarioService {
	UsuarioDTO findByUsername(String username);
	Usuario update(UsuarioDTO usuario, String id);
}
