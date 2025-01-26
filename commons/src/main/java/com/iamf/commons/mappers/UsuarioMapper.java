package com.iamf.commons.mappers;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.models.Persona;
import com.iamf.commons.models.Usuario;

public class UsuarioMapper {

	public Usuario getUsuario(UsuarioDTO usuarioDTO){
	    Usuario usuario = Usuario.builder()
	    		.id(usuarioDTO.getId())
	    		.build();
	    return usuario;
	}

    public UsuarioDTO getUsuarioDTO(Usuario usuario){
		UsuarioDTO usuarioDTO = UsuarioDTO.builder()
        		.id(usuario.getId())
        		.email(usuario.getEmail())
				.username(usuario.getUsername())
        		.password(usuario.getPassword())
				.codigoDeLlamada(usuario.getCodigoDeLlamada())
        		.celular(usuario.getCelular())
        		.roles(usuario.getRoles())
        		.enabled(usuario.getEnabled())
        		.intentos(usuario.getIntentos())
				.nivelDeVerificacion(usuario.getNivelDeVerificacion())
				.emailVerificado(usuario.getEmailVerificado())
				.celularVerificado(usuario.getCelularVerificado())
				.verificacion2Factores(usuario.getVerificacion2Factores())
				.codigoDeVerificacion(usuario.getCodigoDeVerificacion())
				.build();
        return usuarioDTO;
    }

	public Usuario getFullUsuario(UsuarioDTO usuarioDTO){
	    Usuario usuario = Usuario.builder()
	    		.id(usuarioDTO.getId())
	    		.email(usuarioDTO.getEmail())
	    		.username(usuarioDTO.getUsername())
	    		.password(usuarioDTO.getPassword())
	    		.codigoDeLlamada(usuarioDTO.getCodigoDeLlamada())
	    		.celular(usuarioDTO.getCelular())
	    		.roles(usuarioDTO.getRoles())
	    		.enabled(usuarioDTO.getEnabled())
	    		.intentos(usuarioDTO.getIntentos())
	    		.codigoDeVerificacion(usuarioDTO.getCodigoDeVerificacion())
	    		.vencimientoDeCodigoDeVerificacion(usuarioDTO.getVencimientoDeCodigoDeVerificacion())
	    		.fechaDeSolicitudDeCodigoDeVerificacion(usuarioDTO.getFechaDeSolicitudDeCodigoDeVerificacion())
	    		.nivelDeVerificacion(usuarioDTO.getNivelDeVerificacion())
	    		.emailVerificado(usuarioDTO.getEmailVerificado())
	    		.celularVerificado(usuarioDTO.getCelularVerificado())
	    		.verificacion2Factores(usuarioDTO.getVerificacion2Factores())
	    		.build();
	    return usuario;
	}

	public UsuarioDTO getUsuarioDTO(Persona persona){
		PersonaDTO personaDTO = new PersonaDTO();
		personaDTO.setCredenciales(getUsuarioDTO(persona.getCredenciales()));
		personaDTO.getCredenciales().setTipoPersona(persona.getTipoPersona());
		personaDTO.getCredenciales().setNombre(persona.getNombre());
		personaDTO.getCredenciales().setApellido(persona.getApellido());
		return personaDTO.getCredenciales();
	}

}
