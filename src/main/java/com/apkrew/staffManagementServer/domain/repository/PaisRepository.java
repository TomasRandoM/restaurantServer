package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Pais;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisRepository extends BaseRepository<Pais, String> {
    Pais findByNombreAndEliminadoFalse(String nombre);
    boolean existsByNombreAndEliminadoFalse(String nombre);
}
