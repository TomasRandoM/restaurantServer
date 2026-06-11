package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Empresa;

public interface EmpresaRepository extends BaseRepository<Empresa, String> {

    boolean existsByNombreAndEliminadoFalse(String nombre);

    Empresa findByNombreAndEliminadoFalse(String nombre);
}
