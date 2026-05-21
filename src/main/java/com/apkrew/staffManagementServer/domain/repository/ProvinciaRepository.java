package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Provincia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepository extends BaseRepository<Provincia, String> {
    boolean existsByNombreAndPaisIdAndEliminadoFalse(String nombre, String paisId);
    Provincia findByNombreAndPaisIdAndEliminadoFalse(String nombre, String paisId);
    List<Provincia> findByPaisIdAndEliminadoFalse(String paisId);
}