package com.apkrew.staffManagementServer.domain.repository;


import com.apkrew.staffManagementServer.domain.entity.Persona;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends BaseRepository<Persona, String>{
    boolean existsByDniAndEliminadoFalse(String dni);
    boolean existsByEmailAndEliminadoFalse(String email);
    boolean existsByUsuarioIdAndEliminadoFalse(String usuarioId);
    Persona findByDniAndEliminadoFalse(String dni);
    Persona findByEmailAndEliminadoFalse(String email);
}
