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
    private Usuario credenciales;
}
