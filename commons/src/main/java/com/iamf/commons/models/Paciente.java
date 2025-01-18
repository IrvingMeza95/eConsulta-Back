package com.iamf.commons.models;

import com.iamf.commons.enums.TipoPersona;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "pacientes")
public class Paciente extends  Persona {

    @PrePersist
    public void prePersist() {
        setTipoPersona(TipoPersona.PACIENTE);
    }

}
