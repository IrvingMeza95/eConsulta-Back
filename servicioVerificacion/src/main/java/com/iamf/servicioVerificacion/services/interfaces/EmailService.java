package com.iamf.servicioVerificacion.services.interfaces;

import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.dtos.RequestDTO;
import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<?> sendMessage(RequestDTO request);
    ResponseEntity<?> codigoDeVerificacionDeCorreo(RequestDTO request, UsuarioDTO usuarioDTO, Integer codigo) throws MyException;
    ResponseEntity<?> codigoDeVerificacion2Factores(RequestDTO request, UsuarioDTO usuarioDTO, Integer codigo) throws MyException;
    ResponseEntity<?> nuevoLogin(RequestDTO request, UsuarioDTO usuario) throws MyException;
    ResponseEntity<?> emailDeBienvenida(RequestDTO request) throws MyException;
    ResponseEntity<?> correoRecuperacionPassword(RequestDTO request, UsuarioDTO usuarioDTO, Integer codigo) throws MyException;
    ResponseEntity<?> enviarArchivo(RequestDTO request) throws MyException;
}
