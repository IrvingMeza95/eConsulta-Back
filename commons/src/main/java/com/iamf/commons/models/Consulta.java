package com.iamf.commons.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
//    @ManyToOne
//    @JoinColumn(name = "servicio_medico_id", nullable = true)
//    private ServicioMedico servicioMedico;
    @Column(nullable = true)
    private Long idServicioMedico;
    //    @ManyToOne
//    @JoinColumn(name = "paquete_id", nullable = true)
//    private Paquete paquete;
    @Column(nullable = true)
    private Long idPaquete;
    @Column(nullable = false)
    private Double total;
    @Column(nullable = false)
    private Boolean pagado;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "consulta_id", nullable = false)
    private List<ServicioContratado> serviciosContratados;

    @PrePersist
    public void prePersist() {
        setPagado(false);
    }
}
