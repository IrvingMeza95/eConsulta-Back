package com.iamf.commons.enums;

import com.iamf.commons.exceptions.MyException;

public enum Roles {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_SUPER_ADMIN;

    public static Roles validarExistencia(String rol) throws MyException {
        for (Roles r : Roles.values()){
            if (r.name().equals(rol))
                return r;
        }
        throw new MyException("No existe el rol " + rol + ".");
    }

}
