package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Localidad;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends BaseRepository<Localidad, String> {
    Localidad findByNombreAndDepartamentoIdAndEliminadoFalse(String nombre, String departamentoId);
    boolean existsByNombreAndDepartamentoIdAndEliminadoFalse(String nombre, String departamentoId);
}
