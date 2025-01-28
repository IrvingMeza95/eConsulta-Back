package com.iamf.commons.dtos;

import com.iamf.commons.models.Paquete;
import com.iamf.commons.models.ServicioMedico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultaDTO {
    private String id;
    private String fecha;
    private String horario;
    private PersonaDTO medico;
    private PersonaDTO paciente;
    private ServicioMedico servicioMedico;
    private Paquete paquete;
    private Double total;
    private Boolean pagado;
}
