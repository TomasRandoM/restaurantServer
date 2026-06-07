package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.enums.EstadoRegistroHorario;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroHorarioRepository extends BaseRepository<RegistroHorario, String> {
    boolean existsByEmpleadoIdAndFechaEntradaAndEliminadoFalse(String empleadoId, Date fechaEntrada);
    Optional<RegistroHorario> findFirstByEmpleadoIdAndEstadoRegistroHorarioAndEliminadoFalseOrderByFechaSalidaDesc(String empleadoId, EstadoRegistroHorario estado);
    List<RegistroHorario> findAllByEstadoRegistroHorarioAndEliminadoFalse(EstadoRegistroHorario estado);
    List<RegistroHorario> findByEmpleadoIdAndFechaSalidaBetweenAndEliminadoFalse(String empleadoId, Date desde, Date hasta);
}
