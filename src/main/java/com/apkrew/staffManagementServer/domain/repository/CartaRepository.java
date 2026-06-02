package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Carta;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CartaRepository extends BaseRepository<Carta, String> {

    List<Carta> findByFechaDesdeLessThanEqualAndFechaHastaGreaterThanEqualAndEliminadoFalse(
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

}
