package com.iamf.commons.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(nullable = false, length = 50)
    private String fecha;
    @Column(nullable = false, length = 50)
    private String horario;
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    @ManyToOne
    @JoinColumn(name = "servicio_medico_id", nullable = true)
    private ServicioMedico servicioMedico;
    @ManyToOne
    @JoinColumn(name = "paquete_id", nullable = true)
    private Paquete paquete;
    @Column(nullable = false)
    private Double total;
    @Column(nullable = false)
    private Boolean pagado;

    @PrePersist
    public void prePersist() {
        setPagado(false);
    }
}
