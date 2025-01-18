package com.iamf.servicioVerificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.iamf.commons", "com.iamf.servicioEmails"})
public class ServicioVerificacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioVerificacionApplication.class, args);
	}

}
