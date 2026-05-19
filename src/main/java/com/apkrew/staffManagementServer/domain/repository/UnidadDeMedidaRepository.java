package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadDeMedidaRepository extends BaseRepository<UnidadDeMedida, String>{
    UnidadDeMedida findByNombreIgnoreCaseAndEliminadoFalse(String nombre);
    boolean existsByNombreIgnoreCaseAndEliminadoFalse(String nombre);
}
