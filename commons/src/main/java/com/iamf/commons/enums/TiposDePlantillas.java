package com.iamf.commons.enums;

import com.iamf.commons.exceptions.MyException;

public enum TiposDePlantillas {
    BIENVENIDA,
    CODIGO_VERIFICACION_DE_CELULAR,
    CODIGO_VERIFICACION_DE_CORREO,
    EMAILS_VERIFICACION_2_FACTORES,
    CELULAR_VERIFICACION_2_FACTORES,
    CORREO_RECUPERACION_PASSWORD,
    NUEVO_INICIO_DE_SESION;

    public static Boolean validarExistencia(String tipo) throws MyException {
        for (TiposDePlantillas t : TiposDePlantillas.values()){
            if (t.name().equals(tipo))
                return true;
        }
        throw new MyException("No existe una categoría " + tipo + " de plantillas.");
    }

}
