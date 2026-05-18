package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RegistroHorarioRepository extends BaseRepository<RegistroHorario, String> {
    boolean existsByEmpleadoIdAndFechaEntradaAndEliminadoFalse(String empleadoId, Date fechaEntrada);
}
