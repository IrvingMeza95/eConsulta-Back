package com.iamf.servicioUsuarios.repositories;

import com.iamf.commons.enums.NivelDeVerificacion;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.models.Usuario;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, String> {
    @Query("SELECT u FROM Usuario u WHERE u.persona.id =?1 or u.username =?1 or u.email =?1 or CONCAT(u.codigoDeLlamada, u.celular) =?1")
    Optional<Usuario> buscarPorIdEmailCelular(String param);
    @Query("SELECT u.persona.tipoPersona FROM Usuario u WHERE u.id =?1 or u.username =?1 or u.email =?1 or CONCAT(u.codigoDeLlamada, u.celular) =?1")
    Optional<TipoPersona> getTipoPersona(String param);
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.codigoDeVerificacion = :codigo, u.vencimientoDeCodigoDeVerificacion = :fecha WHERE u.email = :email")
    Integer guardarCodigoDeVerificacion(@Param("codigo") Integer codigo, @Param("email") String email, @Param("fecha") String fecha);
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.codigoDeVerificacion = null, u.vencimientoDeCodigoDeVerificacion = null WHERE u.email = :email")
    Integer eliminarCodigoDeVerificacion(@Param("email") String email);
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.nivelDeVerificacion = :nivel WHERE u.email = :email")
    Integer cambiarNivelDeVerificacion(@Param("estatus") NivelDeVerificacion nivel, @Param("email") String email);
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.emailVerificado = ?1 WHERE u.email = ?2")
    Integer cambiarEmailVerificado(Boolean estatus, String email);
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.celularVerificado = ?1 WHERE u.email = ?2")
    Integer cambiarCelularVerificado(Boolean estatus, String email);
}
