package com.iamf.servicioOauth.clients;

import com.iamf.commons.dtos.RequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="servicioVerificacion")
public interface ServicioVerificacion {
    @PostMapping("/emails/nuevo-login")
    ResponseEntity<?> sendEmail(@RequestBody RequestDTO request);
}
