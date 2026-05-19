package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Articulo;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo, String> {
    Articulo findByNombreIgnoreCaseAndEliminadoFalse(String nombre);
    boolean existsByNombreIgnoreCaseAndEliminadoFalse(String nombre);
}
