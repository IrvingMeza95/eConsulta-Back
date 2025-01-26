package com.iamf.servicioVerificacion.services;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import com.google.gson.JsonObject;
import com.iamf.servicioVerificacion.util.GmailApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
public class GmailService {

    @Value("${gmail.client.id}")
    private String clientId;
    @Value("${gmail.client.secret}")
    private String clientSecret;
    @Value("${gmail.refresh.token}")
    private String refreshToken;
    @Value("${gmail.email}")
    private String fromEmail;

    public void sendEmail(MimeMessage mimeMessage) throws Exception {
        log.info("Preparqando para enviar email.");
        String accessToken = getAccessToken();
        log.info("Creando mime message.");
        log.info("Mensaje a codificar: " + mimeMessageToString(mimeMessage));

        log.info("Codificando mensaje.");
        // Convertir el mensaje a Base64 URL-safe
        String encodedEmailStr = encodeMimeMessage(mimeMessage);
        log.info("Email codificado: " + encodedEmailStr);

        // Construir el JSON para la API de Gmail
        JsonObject json = new JsonObject();
        json.addProperty("raw", encodedEmailStr);
        log.info("JSON con el corrreo codificado: " +  json.toString());

        // Enviar el correo mediante la API de Gmail
        GmailApiUtil.sendMessage(accessToken, json.toString());
    }

    private String getAccessToken() throws IOException {
        log.info("Generando nuevo access token.");
        log.info("Cliente ID: " + clientId);
        log.info("Cliente Secreto ID: " + clientSecret);
        log.info("Refresh token: " + refreshToken);
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();

        AccessToken token = credentials.refreshAccessToken();
        log.info("Nuevo access token: " + token.getTokenValue());
        return token.getTokenValue();
    }

    private MimeMessage createEmail(String to, String from, String subject, String body) throws MessagingException {
        log.info("Creando correo, " + to + ", " + from + ", " + subject + ", " + body);
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(body);

        return email;
    }

    public String encodeMimeMessage(MimeMessage mimeMessage) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mimeMessage.writeTo(outputStream);  // Escribir el mensaje MIME en el stream
        byte[] rawBytes = outputStream.toByteArray();  // Obtener bytes del mensaje MIME

        // Codificar en Base64 URL-safe sin padding
        return Base64.getUrlEncoder().withoutPadding().encodeToString(rawBytes);
    }

    public String mimeMessageToString(MimeMessage mimeMessage) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mimeMessage.writeTo(outputStream);
        return outputStream.toString("UTF-8");  // Convierte a cadena con codificación UTF-8
    }

}

