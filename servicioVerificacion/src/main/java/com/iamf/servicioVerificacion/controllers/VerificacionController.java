package com.iamf.servicioVerificacion.controllers;

import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.exceptions.MyException;
import com.iamf.servicioVerificacion.clientes.ServicioUsuarios;
import com.iamf.servicioVerificacion.services.interfaces.EmailService;
import com.iamf.servicioVerificacion.services.interfaces.SmsService;
import com.iamf.servicioVerificacion.util.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verificacion")
@Slf4j
public class VerificacionController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private ServicioUsuarios servicioUsuarios;

    @PostMapping("/codigo-de-verificacion")
    public ResponseEntity<?> codigoDeVerificacion(@RequestBody RequestDTO request) throws MyException {
        log.info("Se solicito un codigo de verificacion.");
        TiposDePlantillas.validarExistencia(request.getTemplate());
        UsuarioDTO usuario = new UsuarioDTO();
        try{
            usuario = servicioUsuarios.getUsuario(request.getTo());
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener al usuario con el email " + request.getTo() + ".");
        }
        Integer codigoDeVerificacion = Utilities.codigoDeVerificacion();
        if (request.getTemplate().equals(TiposDePlantillas.CODIGO_VERIFICACION_DE_CORREO.name())) {
            return emailService.codigoDeVerificacionDeCorreo(request, usuario, codigoDeVerificacion);
        }else if (request.getTemplate().equals(TiposDePlantillas.CODIGO_VERIFICACION_DE_CELULAR.name())){
            return smsService.codigoDeVerificacionDeCelular(request,usuario,codigoDeVerificacion);
        }else if (request.getTemplate().equals(TiposDePlantillas.EMAILS_VERIFICACION_2_FACTORES.name())){
            log.info("Se eligio verificacion por correo.");
            return emailService.codigoDeVerificacion2Factores(request,usuario,codigoDeVerificacion);
        }else if (request.getTemplate().equals(TiposDePlantillas.CELULAR_VERIFICACION_2_FACTORES.name())) {
            log.info("Se eligio verificacion por sms.");
            return smsService.codigoDeVerificacion2Factores(request, usuario, codigoDeVerificacion);
        }else if (request.getTemplate().equals(TiposDePlantillas.CORREO_RECUPERACION_PASSWORD.name())) {
            return emailService.correoRecuperacionPassword(request, usuario, codigoDeVerificacion);
        }
            return null;
    }

}
