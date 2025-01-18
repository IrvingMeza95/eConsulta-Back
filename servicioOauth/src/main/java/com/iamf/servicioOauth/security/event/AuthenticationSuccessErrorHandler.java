package com.iamf.servicioOauth.security.event;

import brave.Tracer;
import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.servicioOauth.clients.ServicioVerificacion;
import com.iamf.servicioOauth.configs.ServiceProperties;
import com.iamf.servicioOauth.services.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import feign.FeignException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private ServicioVerificacion servicioVerificacion;
	@Autowired
	private ServiceProperties serviceProperties;
	@Autowired
	private Tracer tracer;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		if(authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}
		
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String mensaje = "Success Login: " + user.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);

		UsuarioDTO usuario = usuarioService.findByUsername(authentication.getName());
		if(usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}

		if (serviceProperties.getNotificaciones().getEmailNuevoLogin().equalsIgnoreCase("true")){
			log.info("Enviando correo de nuevo inicio de sesion.");
			// Obtener el HttpServletRequest actual
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			// Acceder a los headers del request
			String fechaHora = request.getHeader("fecha-hora");
			log.info("Fecha y hora: " + fechaHora);
			String ubicacion = request.getHeader("ubicacion");
			log.info("Ubicacion" + ubicacion);
			String dispositivo = request.getHeader("dispositivo");
			log.info("Dispositivo: " + dispositivo);

			//Correo de nuevo inicio de sesion
			RequestDTO requestDTO = RequestDTO.builder()
					.to(usuario.getEmail())
					.username(usuario.getUsername())
					.subject("Nuevo inicio de sesión.")
					.template(TiposDePlantillas.NUEVO_INICIO_DE_SESION.name())
					.fecha(fechaHora)
					.ubicacion(ubicacion)
					.dispositivo(dispositivo)
					.build();
			servicioVerificacion.sendEmail(requestDTO);
		}
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String mensaje = "Error en el Login: " + exception.getMessage();
		log.error(mensaje);
		System.out.println(mensaje);

		try {
			
			StringBuilder errors = new StringBuilder();
			errors.append(mensaje);
			
			UsuarioDTO usuario = usuarioService.findByUsername(authentication.getName());
			if (usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			
			log.info("Intentos actual es de: " + usuario.getIntentos());
			
			usuario.setIntentos(usuario.getIntentos()+1);
			
			log.info("Intentos después es de: " + usuario.getIntentos());
			
			errors.append(" - Intentos del login: " + usuario.getIntentos());
			
			if(usuario.getIntentos() >= 3) {
				String errorMaxIntentos = String.format("El usuario %s des-habilitado por máximos intentos.", usuario.getEmail());
				log.error(errorMaxIntentos);
				errors.append(" - " + errorMaxIntentos);
				usuario.setEnabled(false);
			}
			
			usuarioService.update(usuario, usuario.getId());

			tracer.currentSpan().tag("mensaje.error", errors.toString());
		} catch (FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}

	}

}
