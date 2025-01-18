package com.iamf.commons.models;

import com.iamf.commons.enums.NivelDeVerificacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "credenciales_de_usuario")
public class Usuario {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, length = 20, nullable = false)
    private String username;
    @Column(nullable = true)
    private String password;
    @Column(nullable = false)
    private String codigoDeLlamada;
    @Column(unique = true, nullable = false)
    private String celular;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_de_usuario",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = { "usuario_id", "rol_id" }) }
    )
    private List<Role> roles;
    @Column(nullable = false)
    private Boolean enabled;
    @Column(nullable = false)
    private Integer intentos;
    @OneToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;
    @Column(nullable = true, length = 10)
    private Integer codigoDeVerificacion;
    @Column(nullable = true)
    private String vencimientoDeCodigoDeVerificacion;
    @Column(name = "fecha_solicitud_codigo", nullable = true)
    private String fechaDeSolicitudDeCodigoDeVerificacion;
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NivelDeVerificacion nivelDeVerificacion;
    @Column(nullable = false)
    private Boolean emailVerificado;
    @Column(nullable = false)
    private Boolean celularVerificado;
    @Column(nullable = false)
    private Boolean verificacion2Factores;

    @PrePersist
    public void prePersist() {
        enabled = true;
        intentos = 0;
        nivelDeVerificacion = NivelDeVerificacion.SIN_VERIFICAR;
        verificacion2Factores = false;
        emailVerificado = false;
        celularVerificado = false;
    }

}
