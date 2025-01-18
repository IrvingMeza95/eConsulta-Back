//package com.iamf.commons.models;
//
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
//@Entity(name = "cuentas_bancarias")
//public class CuentaBancaria {
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    private String id;
//    @Column(nullable = false, unique = true, length = 20)
//    private String numeroCuenta;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "persona_id", nullable = false)
//    private Persona titular;
////    private List<String> transacciones;
//}
