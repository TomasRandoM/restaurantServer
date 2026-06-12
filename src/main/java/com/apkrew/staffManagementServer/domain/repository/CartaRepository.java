package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Carta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartaRepository extends BaseRepository<Carta, String> {

    Optional<Carta> findByActivoTrueAndEliminadoFalse();

    @Query("""
    SELECT c
    FROM Carta c
    WHERE c.eliminado = false
      AND c.activo = true
    """)
    List<Carta> findAllActivas();
    
}
