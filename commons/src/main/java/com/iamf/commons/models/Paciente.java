package com.iamf.commons.models;

import com.iamf.commons.enums.TipoPersona;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "pacientes")
public class Paciente extends  Persona {

    @Column(nullable = false)
    private Boolean obraSocial;
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    @PrePersist
    public void prePersist() {
        setTipoPersona(TipoPersona.PACIENTE);
    }

}
