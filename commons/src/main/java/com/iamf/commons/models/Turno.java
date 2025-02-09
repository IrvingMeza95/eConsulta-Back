package com.iamf.commons.models;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = false, length = 50)
    private String horario;
    @Column(nullable = false, unique = true, length = 50)
    private String subHorario;
    @Column(nullable = false)
    private Boolean enabled;

    @PrePersist
    public void prePersist() {
        setEnabled(true);
    }
}
