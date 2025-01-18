package com.iamf.servicioUsuarios.clientes;

import com.iamf.commons.models.Chat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="servicioChats")
public interface ServicioChats {
    @GetMapping("/chats/{id}")
    Chat getChat(@PathVariable String id);
}
