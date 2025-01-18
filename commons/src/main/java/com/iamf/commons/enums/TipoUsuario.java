package com.iamf.commons.enums;

import com.iamf.commons.exceptions.MyException;

public enum TipoUsuario {
    MEDICO,
    PACIENTE;

    public static TipoUsuario validar(String tipo) throws MyException {
        for (TipoUsuario tu : TipoUsuario.values()){
            if (tu.name().equalsIgnoreCase(tipo)){
                return tu;
            }
        }
        throw new MyException("Hubo un problema al registrar el tipo de usuario.");
    }

}
