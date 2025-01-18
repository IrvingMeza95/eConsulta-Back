//package com.iamf.commons.models;
//
//import com.iamf.commons.enums.TipoTarjeta;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.GenericGenerator;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity(name = "tarjetas_bancarias")
//public class TarjetaBancaria {
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    private String id;
//    @Column(nullable = false, unique = true, length = 20)
//    private String numeroTarjeta;
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "persona_id")
//    private Persona titular;
//    @Column(nullable = false, length = 10)
//    private String fechaVencimiento;
//    @Column(nullable = false, length = 5)
//    private String codigoSeguridad;
//    @Column(nullable = false, length = 25)
//    private String bancoEmisor;
//    @Column(nullable = false, length = 10)
//    @Enumerated(EnumType.STRING)
//    private TipoTarjeta tipoTarjeta;
//}
