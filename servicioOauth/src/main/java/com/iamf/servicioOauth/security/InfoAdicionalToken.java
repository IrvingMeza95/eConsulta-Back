package com.iamf.servicioOauth.security;

import java.util.HashMap;
import java.util.Map;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.servicioOauth.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class InfoAdicionalToken implements TokenEnhancer{

	@Autowired
	private IUsuarioService usuarioService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<String, Object>();
		
		UsuarioDTO usuario = usuarioService.findByUsername(authentication.getName());
		info.put("username", usuario.getUsername());
		info.put("correo", usuario.getEmail());
		info.put("verificacion2Factores", usuario.getVerificacion2Factores());
		info.put("TipoPersona",usuario.getTipoPersona());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

}
