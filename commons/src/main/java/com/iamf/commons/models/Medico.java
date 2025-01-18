package com.iamf.commons.models;

import com.iamf.commons.enums.TipoPersona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "medico")
public class Medico extends  Persona {

    @Column(nullable = false)
    private  String especialidad;
    @Column(nullable = false)
    private double sueldo;

    @PrePersist
    public void prePersist() {
        setTipoPersona(TipoPersona.MEDICO);
    }

}
