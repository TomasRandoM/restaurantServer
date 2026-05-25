package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Departamento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepository extends BaseRepository<Departamento, String> {

    boolean existsByNombreAndProvinciaIdAndEliminadoFalse(
            String nombre,
            String provinciaId
    );

    Departamento findByNombreAndProvinciaIdAndEliminadoFalse(
            String nombre,
            String provinciaId
    );

    List<Departamento> findByProvinciaIdAndEliminadoFalse(String provinciaId);
}
