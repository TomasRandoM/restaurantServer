package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReciboDeSueldoRepository extends BaseRepository<ReciboDeSueldo, String> {
    List<ReciboDeSueldo> findByEmpleadoIdAndEliminadoFalse(String empleadoId);
    Page<ReciboDeSueldo> findByEmpleadoIdAndEliminadoFalse(String empleadoId, Pageable pageable);
    ReciboDeSueldo findByEmpleadoIdAndMesPagoAndEliminadoFalse(String empleadoId, Integer mesPago);
}
