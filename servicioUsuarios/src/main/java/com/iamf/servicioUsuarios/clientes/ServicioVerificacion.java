package com.iamf.servicioUsuarios.clientes;

import com.iamf.commons.dtos.RequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="servicioVerificacion")
public interface ServicioVerificacion {
    @PostMapping("/verificacion/codigo-de-verificacion")
    ResponseEntity<?> codigoDeVerificacion(@RequestBody RequestDTO request);
    @PostMapping("/emails/bienvenida")
    ResponseEntity<?> emailDeBienvenida(@RequestBody RequestDTO Request);
}
