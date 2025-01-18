package com.iamf.servicioUsuarios.controllers;

import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.enums.NivelDeVerificacion;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.services.interfaces.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/verificacion")
@Slf4j
public class VerificacionController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping
    public ResponseEntity<ResponseMessage> verificar(@RequestBody RequestDTO requeest) throws MyException {
        log.info("Verificando al usuario con el identificador: " + requeest.getTo());
        if (usuarioService.verificar(requeest.getTo(), requeest.getTemplate(),requeest.getCodigo(), requeest.getFecha())){
            return ResponseEntity.ok(new ResponseMessage("Verificación exitosa."));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseMessage("Verificación fallida."));
        }
    }

    @GetMapping("/niveles-de-verificacion")
    public List<NivelDeVerificacion> nivelesDeVerificacion(){
        return List.of(NivelDeVerificacion.values());
    }

}
