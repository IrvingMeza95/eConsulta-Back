package com.iamf.servicioVerificacion.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class GmailApiUtil {
    private static final String GMAIL_API_URL = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    public static void sendMessage(String accessToken, String emailJson) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(emailJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(GMAIL_API_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Correo enviado correctamente.");
        } else {
            System.err.println("Error al enviar el correo: " + response.getBody());
        }
    }
}

