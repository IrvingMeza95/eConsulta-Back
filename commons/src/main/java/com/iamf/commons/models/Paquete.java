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
@Entity(name = "paquetes")
public class Paquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "paquete_servicio", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "paquete_id"), // Clave foránea hacia PaqueteMedico
            inverseJoinColumns = @JoinColumn(name = "servicio_medico_id") // Clave foránea hacia ServicioMedico
    )
    private List<ServicioMedico> servicios;
    @Column(nullable = false)
    private Double precio;
    @Column(nullable = false)
    private Boolean enabled;

    @PrePersist
    public void prePersist() {
        setEnabled(true);
    }
}
