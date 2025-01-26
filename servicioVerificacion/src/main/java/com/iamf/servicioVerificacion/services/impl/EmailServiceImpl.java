package com.iamf.servicioVerificacion.services.impl;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.utils.EmailUtils;
import com.iamf.servicioVerificacion.clientes.ServicioUsuarios;
import com.iamf.servicioVerificacion.configs.ServiceProperties;
import com.iamf.servicioVerificacion.dtos.Body;
import com.iamf.commons.dtos.MetaData;
import com.iamf.commons.dtos.RequestDTO;
import com.iamf.servicioVerificacion.services.GmailService;
import com.iamf.servicioVerificacion.services.interfaces.EmailService;
import com.iamf.servicioVerificacion.util.Utilities;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ServicioUsuarios servicioUsuarios;
    @Autowired
    private GmailService gmailService;
    @Autowired
    private ServiceProperties serviceProperties;

    @Override
    public ResponseEntity<?> sendMessage(RequestDTO request) {
        try {
            if (EmailUtils.isValidEmail(request.getTo())) {
                String messageContent = request.getTemplate();
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper;
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(Utilities.FROM);
                helper.setTo(request.getTo());
                helper.setSubject(request.getSubject());
                helper.setText(messageContent, true);
                gmailService.sendEmail(message);
            }else {
                return new ResponseEntity<Body>(new Body("Correo electronico invalido!") , HttpStatus.NOT_ACCEPTABLE);
            }

        } catch (NotAcceptableStatusException e1) {
            return new ResponseEntity<Body>(new Body(e1.getReason()) , HttpStatus.NOT_ACCEPTABLE);
        } catch (MessagingException e) {
            return new ResponseEntity<Body>(new Body(e.getMessage() ), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> codigoDeVerificacionDeCorreo(RequestDTO request, UsuarioDTO usuario, Integer codigoDeVerificacion) throws MyException {
        log.info("Generando codigo de verificacion de correo.");
        request.setSubject("Creación de contraseña.");
        log.info("Generando fecha de expiracion para codigo de verificacion de email.");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String fechaDeExpiracion = Utilities.obtenerFechaDeExpiracionDeCodigo(now.format(formatter),
                serviceProperties.getValidationCodeDuration());
        log.info("Codigo de verificacion: " + codigoDeVerificacion);
        Integer codigoRespuesta = servicioUsuarios.guardarCodigoDeVerificacion(request.getTo(), codigoDeVerificacion,fechaDeExpiracion);
        log.info("Codigo respuesta: " + codigoRespuesta);
        if (codigoRespuesta == null || codigoRespuesta < 1)
            throw new MyException("Error al generar el código de verificación.");
        request.setMetaData(new ArrayList<MetaData>());
        request.getMetaData().add(MetaData.builder()
                .key("nombreUsuario")
                .value(usuario.getNombre() + " " + usuario.getApellido())
                .build());
        String urlAgregarPassword = serviceProperties.getUrlAgregarPassword().replace("username", usuario.getUsername());
        String urlFinal = urlAgregarPassword.replace("codigo", String.valueOf(codigoDeVerificacion));
        request.getMetaData().add(MetaData.builder()
                .key("urlAgregarPassword")
                .value(urlFinal)
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("fechaExpiracion")
                .value(fechaDeExpiracion)
                .build());
        request.setTemplate(templateService.buildMessage(request));
        log.info("Enviando correo.");
        sendMessage(request);
        return new ResponseEntity<Body>(new Body("Se envió un correo de verificación de cuenta a la dirección "
                + request.getTo() + "."), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> codigoDeVerificacion2Factores(RequestDTO request, UsuarioDTO usuario, Integer codigoDeVerificacion) throws MyException {
        log.info("Generando codigo de verificacion de 2 factores.");
        request.setSubject("Código de verificación de 2 factores.");
        log.info("Generando fecha de expiracion para codigo de verificacion de 2 factores.");
        String fechaDeExpiracion = Utilities.obtenerFechaDeExpiracionDeCodigo(request.getFecha(),
                serviceProperties.getTwoFactoresValidationCodeDuration());
        log.info("Codigo de verificacion: " + codigoDeVerificacion);
        Integer codigoRespuesta = servicioUsuarios.guardarCodigoDeVerificacion(request.getTo(), codigoDeVerificacion,fechaDeExpiracion);
        log.info("Codigo respuesta: " + codigoRespuesta);
        if (codigoRespuesta == null || codigoRespuesta < 1)
            throw new MyException("Error al generar el código de verificación.");
        request.setMetaData(new ArrayList<MetaData>());
        request.getMetaData().add(MetaData.builder()
                .key("nombreUsuario")
                .value(usuario.getUsername())
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("codigo")
                .value(String.valueOf(codigoDeVerificacion))
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("fechaExpiracion")
                .value(fechaDeExpiracion)
                .build());
        request.setTemplate(templateService.buildMessage(request));
        log.info("Enviando correo.");
        sendMessage(request);
        return new ResponseEntity<Body>(new Body("Se envió un código de verificación de 2 factores al correo "
                + request.getTo() + "."), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> nuevoLogin(RequestDTO request) throws MyException {
        log.info("Preparando plantilla " + request.getTemplate());
        TiposDePlantillas.validarExistencia(request.getTemplate());
        request.setMetaData(new ArrayList<MetaData>());
        request.getMetaData().add(MetaData.builder()
                .key("nombreUsuario")
                .value(request.getUsername())
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("fechaHora")
                .value(request.getFecha())
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("ubicacion")
                .value(request.getUbicacion())
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("dispositivo")
                .value(request.getDispositivo())
                .build());
        request.setTemplate(templateService.buildMessage(request));
        log.info("Plantilla html terminada: \n" + request.getTemplate());
        sendMessage(request);
        return new ResponseEntity<Body>(new Body("Enviado éxitosamente!"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> emailDeBienvenida(RequestDTO request) throws MyException {
        UsuarioDTO usuario = servicioUsuarios.getUsuario(request.getTo());
        log.info("Preparando plantilla html...");
        request.setSubject("¡Registro exitoso!");
        request.setTemplate(String.valueOf(TiposDePlantillas.BIENVENIDA));
        request.setMetaData(new ArrayList<MetaData>());
        request.getMetaData().add(
                MetaData.builder()
                        .key("nombreUsuario")
                        .value(usuario.getUsername())
                        .build()
        );
        request.setTemplate(templateService.buildMessage(request));
        log.info("Plantilla html terminada: \n" + request.getTemplate());
        sendMessage(request);
        log.info("Se cambui a true la propiedad emailVerifficado.");
        return new ResponseEntity<Body>(new Body("Email de bienvenida enviado éxitosamente!"), HttpStatus.CREATED);
    }

}
