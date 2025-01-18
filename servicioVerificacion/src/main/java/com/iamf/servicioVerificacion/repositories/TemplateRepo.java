package com.iamf.servicioVerificacion.repositories;

import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TemplateRepo extends JpaRepository<Template, String> {
    @Query("SELECT t FROM Template t WHERE t.tipo = ?1")
    Optional<Template> buscarPorTipo(String tipo);
}
