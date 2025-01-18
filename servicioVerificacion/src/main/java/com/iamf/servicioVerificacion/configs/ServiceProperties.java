package com.iamf.servicioVerificacion.configs;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {
    private Integer validationCodeDuration;
    private Integer twoFactoresValidationCodeDuration;
    private String phoneSmsPermited;
    private String whatsappPermited;
}
