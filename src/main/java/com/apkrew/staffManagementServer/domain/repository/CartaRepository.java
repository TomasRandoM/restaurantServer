package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Carta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CartaRepository extends BaseRepository<Carta, String> {

    List<Carta> findByFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualAndEliminadoFalse(
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

    @Query("""
    SELECT c
    FROM Carta c
    WHERE c.eliminado = false
      AND c.fechaDesde <= :fechaHasta
      AND c.fechaHasta >= :fechaDesde
    """)
    List<Carta> buscarCartasSolapadas(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta);
}
