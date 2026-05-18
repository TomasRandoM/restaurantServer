package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Direccion;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends BaseRepository<Direccion, String> {
    Direccion findByCalleAndNumeracionAndLocalidadIdAndEliminadoFalse(String calle, String numeracion, String localidadId);
    boolean existsByCalleAndNumeracionAndLocalidadIdAndEliminadoFalse(String calle, String numeracion, String localidadId);
}
