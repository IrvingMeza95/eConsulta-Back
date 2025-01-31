package com.iamf.servicioConsultas.repositories;

import com.iamf.commons.models.Paquete;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaqueteRepo extends JpaRepository<Paquete, Long> {
    @Query("""
        SELECT p FROM Paquete p
        WHERE 
            (SELECT COUNT(s) FROM p.servicios s WHERE s.id IN :servicioIds) = :size
        AND 
            (SELECT COUNT(s) FROM p.servicios s) = :size
    """)
    List<Paquete> buscarPaquetePorServiciosIds(@Param("servicioIds") List<Long> servicioIds,
                                               @Param("size") long size);

}
