package com.iamf.commons.dtos;

import com.iamf.commons.models.ServicioContratado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultaDTO {
    private Long id;
    private String fecha;
    private String horario;
    private PersonaDTO medico;
    private PersonaDTO paciente;
//    private ServicioMedico servicioMedico;
    private Long idServicioMedico;
//    private Paquete paquete;
    private Long idPaquete;
    private Double total;
    private Boolean pagado;
    private List<ServicioContratado> serviciosContratados;
}
