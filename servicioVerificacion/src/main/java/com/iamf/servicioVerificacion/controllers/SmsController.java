package com.iamf.servicioVerificacion.controllers;

import com.iamf.servicioVerificacion.services.interfaces.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phones")
@Slf4j
public class SmsController {

    private final SmsService smsService;

    @Autowired
    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

//    @PostMapping("/sms")
//    public ResponseEntity<ResponseMessage> sendSms(@RequestBody RequestDTO request) {
//
//        if (TiposDePlantillas.CELULAR_VERIFICACION_2_FACTORES.name().equals(request.getTemplate())){
//
//        }
//        return ResponseEntity.ok(new ResponseMessage("SMS enviado exitósamente."));
//    }

}
