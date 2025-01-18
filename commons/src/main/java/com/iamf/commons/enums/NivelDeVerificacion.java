package com.iamf.commons.enums;

import com.iamf.commons.exceptions.MyException;

public enum NivelDeVerificacion {
    SIN_VERIFICAR,
    BASICO,
    MEDIO,
    ALTO;

    public static Boolean validar(String estatus) throws MyException {
        for (NivelDeVerificacion e : NivelDeVerificacion.values()){
            if (estatus.equals(e.name()))
                return true;
        }
        throw new MyException("Estatus de verificación no valido.");
    }

}
