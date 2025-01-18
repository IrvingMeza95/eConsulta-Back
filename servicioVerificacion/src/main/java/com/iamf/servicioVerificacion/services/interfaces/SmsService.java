package com.iamf.servicioVerificacion.services.interfaces;

import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.exceptions.MyException;
import org.springframework.http.ResponseEntity;

public interface SmsService {
    void sendSms(UsuarioDTO usuario, String message) throws MyException;
    void sendWhatsAppMessage(UsuarioDTO usuario, String body) throws MyException;
    void mensajeDeVerificacionDeNumero(RequestDTO request) throws MyException;
    ResponseEntity<?> codigoDeVerificacionDeCelular(RequestDTO request, UsuarioDTO usuario, Integer codigo) throws MyException;
    ResponseEntity<?> codigoDeVerificacion2Factores(RequestDTO request, UsuarioDTO usuario, Integer codigo) throws MyException;
}
