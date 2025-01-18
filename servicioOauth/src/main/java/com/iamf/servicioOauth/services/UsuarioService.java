package com.iamf.servicioOauth.services;

import java.util.List;
import java.util.stream.Collectors;

import brave.Tracer;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iamf.servicioOauth.clients.ServicioUsuaruis;

import feign.FeignException;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	private Logger log = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private ServicioUsuaruis client;
	@Autowired
	private Tracer tracer;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			UsuarioDTO usuario = findByUsername(username);

			if (usuario != null){
				log.info("Datos de usuario cargados correctamente.");
			}else{
				log.error("No se han podido cargar los datos del usuario.");
			}

			List<GrantedAuthority> authorities = usuario.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getNombre().name()))
					.peek(authority -> log.info("Role: " + authority.getAuthority())).collect(Collectors.toList());

			log.info("Usuario autenticado: " + username);

			return new User(usuario.getEmail(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
					authorities);

		} catch (FeignException e) {
			String error = "Error en el login, no existe el usuario '" + username + "' en el sistema";
			log.error(error);
			tracer.currentSpan().tag("mensaje.error", error + ": " + e.getMessage());
			throw new UsernameNotFoundException(error);
		}
	}

	@Override
	public UsuarioDTO findByUsername(String username) {
		log.info("Buscando el username: " + username);
		return client.getUsuario(username);
	}

	@Override
	public Usuario update(UsuarioDTO usuario, String id) {
		return client.guardar(usuario, id);
	}

}
