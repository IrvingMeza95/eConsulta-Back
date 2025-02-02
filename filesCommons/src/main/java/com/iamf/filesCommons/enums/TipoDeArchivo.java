package com.iamf.filesCommons.enums;

public enum TipoDeArchivo {
    PROFILE_PICTURE,
    RECIBO,
    FACTURA;

    public static  Boolean validarTipoDeArchivo(String tipoDeArchivo){
        for (TipoDeArchivo t : TipoDeArchivo.values()){
            if (String.valueOf(t).equals(tipoDeArchivo) || tipoDeArchivo.contains(t.name()))
                return true;
        }
        return false;
    }

}
