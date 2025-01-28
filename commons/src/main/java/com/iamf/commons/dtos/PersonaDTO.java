package com.iamf.commons.dtos;

import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.models.Turno;
import com.iamf.filesCommons.responses.ResponseFile;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonaDTO {
    //Atributos persona
    private String id;
    private String pais;
    private String ciudad;
    private String direccion;
    private String numeroExterior;
    private String codigoPostal;
    private UsuarioDTO credenciales;
    private Boolean verificado;
    private TipoPersona tipoPersona;
    private List<ResponseFile> archivos;
    private String dni;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    //Atributos de medico
    private double sueldo;
    private  String especialidad;
    private List<Turno> turnos;
    private List<ConsultaDTO> consultas;
}
