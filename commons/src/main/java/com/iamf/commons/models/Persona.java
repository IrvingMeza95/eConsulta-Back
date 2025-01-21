package com.iamf.commons.models;

import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String fechaNacimiento;
    @Column(nullable = true, length = 50)
    private String pais;
    @Column(nullable = true, length = 50)
    private String ciudad;
    @Column(nullable = true, length = 100)
    private String direccion;
    @Column(nullable = true, length = 20)
    private String numeroExterior;
    @Column(name = "cp", nullable = true, length = 20)
    private String codigoPostal;
    @Enumerated(EnumType.STRING)
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    @JoinColumn(name = "id_credenciales_de_usuario", nullable = false)
    private Usuario credenciales;
    @ElementCollection
    private List<String> archivos;
    @Column(nullable = false)
    private Boolean verificado;
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TipoPersona tipoPersona;

}
