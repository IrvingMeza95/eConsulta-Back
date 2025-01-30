package com.iamf.servicioUsuarios.dtos;

import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.models.Usuario;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroDTO {
    private TipoPersona tipoPersona;
    private String dni;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String especialidad;
    private Double sueldo;
    private Usuario credenciales;
    private Boolean obraSocial;
}
