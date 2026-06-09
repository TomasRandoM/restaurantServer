package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Cliente;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente, String> {
    public boolean existsByDniAndEliminadoFalse(String dni);
    public Cliente findByDniAndEliminadoFalse(String dni);
}
