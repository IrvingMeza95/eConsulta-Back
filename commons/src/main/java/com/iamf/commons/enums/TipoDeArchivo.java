package com.iamf.commons.enums;

public enum TipoDeArchivo {
    PROFILE_PICTURE,
    OTRO;

    public static  Boolean validarTipoDeArchivo(String tipoDeArchivo){
        for (TipoDeArchivo t : TipoDeArchivo.values()){
            if (String.valueOf(t).equals(tipoDeArchivo))
                return true;
        }
        return false;
    }

}
