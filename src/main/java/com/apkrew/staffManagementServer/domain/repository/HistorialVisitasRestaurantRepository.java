package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Cliente;
import com.apkrew.staffManagementServer.domain.entity.HistorialVisitasRestaurant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistorialVisitasRestaurantRepository extends BaseRepository<HistorialVisitasRestaurant, String> {
    Optional<HistorialVisitasRestaurant> findByClienteAndEliminadoFalse(Cliente cliente);
}
