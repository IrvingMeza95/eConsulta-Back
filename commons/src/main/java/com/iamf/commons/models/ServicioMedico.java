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
@Entity(name = "servicios_medicos")
public class ServicioMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, length = 250)
    private String descripcion;
    @Column(nullable = false)
    private Double precio;
    @OneToOne
    @JoinColumn(name = "tipo_servicio_id", referencedColumnName = "id")
    private TipoServicio tipoServicio;
}
