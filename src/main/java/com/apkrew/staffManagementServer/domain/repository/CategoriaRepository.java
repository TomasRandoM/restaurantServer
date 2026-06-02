package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Categoria;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria, String> {

    boolean existsByNombreAndEliminadoFalse(String nombre);
    Categoria findByNombreAndEliminadoFalse(String nombre);
}
