package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Rol;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends BaseRepository<Rol,String>{
    boolean existsByNombreAndEliminadoFalse(String nombre);
    Rol findByNombreAndEliminadoFalse(String nombre);
}