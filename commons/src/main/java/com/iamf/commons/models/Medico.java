package com.iamf.commons.models;

import com.iamf.commons.enums.TipoPersona;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicos")
public class Medico extends  Persona {

    @Column(nullable = true)
    private  String especialidad;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "medico_turno",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "turno_id")
    )
    private List<Turno> turnos;
    @Column(nullable = false)
    private double sueldo;
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    @PrePersist
    public void prePersist() {
        setTipoPersona(TipoPersona.MEDICO);
    }

}
