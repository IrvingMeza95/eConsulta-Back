package com.iamf.gatewayServer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebFluxSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	@Value("${config.security.cors.origins}")
	private List<String> origin;
	@Value("${config.security.cors.methods}")
	private List<String> methods;
	@Value("${config.security.cors.headers}")
	private List<String> headers;

	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		return http.authorizeExchange()
				.pathMatchers("/api/security/oauth/**", "/api/wsChats/**", "/api/usuarios/verificacion/**",
						"/api/verificacion/**","/api/usuarios/usuarios/agregar-password/**").permitAll()
				.pathMatchers(HttpMethod.POST,"/api/usuarios/persona").permitAll()
				.pathMatchers(HttpMethod.GET,"/api/usuarios/persona/**", "/api/files/**", "/api/chats/**")
				.hasAnyRole("ADMIN","USER")
				.pathMatchers(HttpMethod.DELETE, "/api/usuarios/persona/{param}")
				.hasRole("ADMIN")
				.pathMatchers("/api/usuarios/usuarios/**", "/api/phones/**").hasRole("ADMIN")
				.anyExchange().authenticated()
				.and().cors().configurationSource(corsConfigurationSource())
				.and().cors().configurationSource(corsConfigurationSource())
				.and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.csrf().disable()
				.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(origin);
		corsConfig.setAllowedMethods(methods);
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(headers);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

}
