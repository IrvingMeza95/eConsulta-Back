package com.iamf.commons.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ServicioVerificacionUtils {

    public static final String FROM = "Aureo.Envios";

    public static String obtenerFechaDeExpiracionDeCodigo(String fechaSolicitud, Integer minutos){
        // Definir el formato deseado para la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Obtener la fecha y hora actual
//        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now = LocalDateTime.parse(fechaSolicitud, formatter);
        // Agregar n minutos a la fecha y hora actual
        LocalDateTime expirationTime = now.plusMinutes(minutos);
        // Formatear la fecha de expiración como cadena
        return expirationTime.format(formatter);
    }

    public static Integer codigoDeVerificacion(){
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

}
