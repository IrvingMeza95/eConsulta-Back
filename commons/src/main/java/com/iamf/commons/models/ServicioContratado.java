package com.iamf.commons.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "servicios_contratados")
public class ServicioContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = false, length = 100)
    private String nombre;
    @Column(nullable = false, unique = false, length = 250)
    private String descripcion;
    @Column(nullable = false)
    private Double precio;
    @Column(nullable = false)
    private Double porcentajeDescuentoPaquete;
    @Column(nullable = false)
    private Double porcentajeDescuentoObraSocial;
    @Column(nullable = false)
    private Double total;
}
