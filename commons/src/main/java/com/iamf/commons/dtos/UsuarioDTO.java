package com.iamf.commons.dtos;

import com.iamf.commons.enums.NivelDeVerificacion;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.models.Persona;
import com.iamf.commons.models.Role;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String id;
    private Persona persona;
private TipoPersona tipoPersona;
    private String email;
    private String username;
    private String password;
    private String codigoDeLlamada;
    private String celular;
    private List<Role> roles;
    private Boolean enabled;
    private Integer intentos;
    private Integer codigoDeVerificacion;
    private String vencimientoDeCodigoDeVerificacion;
    private String fechaDeSolicitudDeCodigoDeVerificacion;
    private NivelDeVerificacion nivelDeVerificacion;
    private Boolean emailVerificado;
    private Boolean celularVerificado;
    private Boolean verificacion2Factores;
    private String nombre;
    private String apellido;
}
