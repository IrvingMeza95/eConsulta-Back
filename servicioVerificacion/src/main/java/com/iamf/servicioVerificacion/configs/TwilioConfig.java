package com.iamf.servicioVerificacion.configs;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.auth.token}")
    private String authToken;
    @Value("${twilio.phone.number}")
    private String fromNumber;
    @Value("${twilio.whatsapp.number}")
    private String fromWhatsAppNumber;

    public TwilioConfig(@Value("${twilio.account.sid}") String accountSid,
                        @Value("${twilio.auth.token}") String authToken,
                        @Value("${twilio.phone.number}") String fromNumber,
                        @Value("${twilio.whatsapp.number}") String fromWhatsAppNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
        this.fromWhatsAppNumber = fromWhatsAppNumber;
        Twilio.init(accountSid, authToken);
    }

    @Bean
    public String fromNumber() {
        return fromNumber;
    }

    @Bean
    public String fromWhatsAppNumber() {
        return fromWhatsAppNumber;
    }

}
