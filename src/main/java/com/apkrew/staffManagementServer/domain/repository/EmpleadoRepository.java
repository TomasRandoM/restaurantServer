package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Empleado;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends BaseRepository<Empleado, String> {
    boolean existsByDniAndEliminadoFalse(String dni);
    Empleado findByDniAndEliminadoFalse(String dni);
}
