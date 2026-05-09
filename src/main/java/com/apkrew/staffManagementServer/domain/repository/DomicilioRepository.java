package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Domicilio;
import org.springframework.stereotype.Repository;

@Repository
public interface DomicilioRepository extends BaseRepository<Domicilio, String> {
    Domicilio findByNombreAndNumeroAndCpAndLocalidadIdAndEliminadoFalse(String nombre, String numero, String cp, String localidadId);
    boolean existsByNombreAndNumeroAndCpAndLocalidadIdAndEliminadoFalse(String nombre, String numero, String cp, String localidadId);
}
