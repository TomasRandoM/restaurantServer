package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Localidad;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalidadRepository extends BaseRepository<Localidad, String> {
    Localidad findByNombreAndDepartamentoIdAndEliminadoFalse(String nombre, String departamentoId);
    List<Localidad> findByDepartamentoIdAndEliminadoFalse(String departamentoId);
    boolean existsByNombreAndDepartamentoIdAndEliminadoFalse(String nombre, String departamentoId);
}
