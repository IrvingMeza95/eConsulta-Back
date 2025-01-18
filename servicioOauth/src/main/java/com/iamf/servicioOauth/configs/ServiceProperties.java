package com.iamf.servicioOauth.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {
    private Notificaciones notificaciones;

    @Setter
    @Getter
    public static class Notificaciones {
        private String emailNuevoLogin;
    }
}
