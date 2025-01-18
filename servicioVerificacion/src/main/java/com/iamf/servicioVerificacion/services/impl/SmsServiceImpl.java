package com.iamf.servicioVerificacion.services.impl;

import com.iamf.commons.dtos.MetaData;
import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.servicioVerificacion.clientes.ServicioUsuarios;
import com.iamf.servicioVerificacion.configs.ServiceProperties;
import com.iamf.servicioVerificacion.configs.TwilioConfig;
import com.iamf.servicioVerificacion.dtos.Body;
import com.iamf.servicioVerificacion.services.interfaces.SmsService;
import com.iamf.servicioVerificacion.util.Utilities;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final TwilioConfig twilioConfig;
    @Autowired
    private ServicioUsuarios servicioUsuarios;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ServiceProperties serviceProperties;

    @Autowired
    public SmsServiceImpl(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void sendSms(UsuarioDTO usuario, String message) throws MyException {
        log.info("Estatus de sms.permited: " + serviceProperties.getPhoneSmsPermited());
        if (serviceProperties.getPhoneSmsPermited().equalsIgnoreCase("false"))
            throw new MyException("El servicio de envío de sms est+á fuera de servicio.");
        log.info("Preparando sms para usuario: " + usuario.getUsername());
        String celular = usuario.getCodigoDeLlamada() + usuario.getCelular();
        log.info("Numero telefonico: " + celular);
        Message.creator(
                new PhoneNumber(celular),
                new PhoneNumber(twilioConfig.fromNumber()),
                message
        ).create();
    }

    @Override
    public void sendWhatsAppMessage(UsuarioDTO usuario, String body) throws MyException {
        log.info("Estatus de whatsapp.permited: " + serviceProperties.getWhatsappPermited());
        if (serviceProperties.getWhatsappPermited().equalsIgnoreCase("false"))
            throw new MyException("El servicio de envío de whatsapps est+á fuera de servicio.");
        log.info("Preparando whatsapp para usuario: " + usuario.getUsername());
        String celular = usuario.getCodigoDeLlamada() + usuario.getCelular();
        log.info("Numero telefonico: " + celular);
        Message message = Message.creator(
                        new PhoneNumber("whatsapp:" + celular),
                        new PhoneNumber(twilioConfig.fromWhatsAppNumber()),
                        body)
                .create();
        log.info("Message SID: " + message.getSid());
    }

    @Override
    public void mensajeDeVerificacionDeNumero(RequestDTO request) throws MyException {
        UsuarioDTO usuarioDTO = servicioUsuarios.getUsuario(request.getTo());
        String message = templateService.buildMessage(request);
        sendSms(usuarioDTO,message);
        sendWhatsAppMessage(usuarioDTO,message);
    }

    @Override
    public ResponseEntity<?> codigoDeVerificacionDeCelular(RequestDTO request, UsuarioDTO usuario, Integer codigo) throws MyException {
        log.info("Generando codigo de verificacion de celular.");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String fechaDeExpiracion = Utilities.obtenerFechaDeExpiracionDeCodigo(now.format(formatter),
                serviceProperties.getValidationCodeDuration());
        log.info("Codigo de verificacion: " + codigo);
        Integer codigoRespuesta = servicioUsuarios.guardarCodigoDeVerificacion(request.getTo(), codigo,fechaDeExpiracion);
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
                .value(String.valueOf(codigo))
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("fechaExpiracion")
                .value(fechaDeExpiracion)
                .build());
        request.setTemplate(templateService.buildMessage(request));
        log.info("SMS: \n"
        + request.getTemplate());
        log.info("Enviando sms.");
        sendSms(usuario,request.getTemplate());
        sendWhatsAppMessage(usuario,request.getTemplate());
        return new ResponseEntity<Body>(new Body("Se envió un código de verificación al numero "
                + usuario.getCodigoDeLlamada() + usuario.getCelular() + "."), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> codigoDeVerificacion2Factores(RequestDTO request, UsuarioDTO usuario, Integer codigo) throws MyException {
        log.info("Generando codigo de verificacion de 2 factores.");
        log.info("Generando fecha de expiracion para codigo de verificacion de 2 factores.");
        String fechaDeExpiracion = Utilities.obtenerFechaDeExpiracionDeCodigo(request.getFecha(),
                serviceProperties.getTwoFactoresValidationCodeDuration());
        log.info("Codigo de verificacion: " + codigo);
        Integer codigoRespuesta = servicioUsuarios.guardarCodigoDeVerificacion(request.getTo(), codigo,fechaDeExpiracion);
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
                .value(String.valueOf(codigo))
                .build());
        request.getMetaData().add(MetaData.builder()
                .key("fechaExpiracion")
                .value(fechaDeExpiracion)
                .build());
        request.setTemplate(templateService.buildMessage(request));
        log.info("SMS: \n"
                + request.getTemplate());
        log.info("Enviando sms.");
        log.info("Enviando sms.");
        sendSms(usuario,request.getTemplate());
        sendWhatsAppMessage(usuario,request.getTemplate());
        return new ResponseEntity<Body>(new Body("Se envió un código de verificación de 2 factores al numero "
                + usuario.getCodigoDeLlamada() + usuario.getCelular() + "."), HttpStatus.CREATED);
    }

}
