package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.ItemReciboDeSueldo;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemReciboDeSueldoRepository extends BaseRepository<ItemReciboDeSueldo, String> {

    ItemReciboDeSueldo findByNombreIgnoreCaseAndEliminadoFalse(String nombre);

    boolean existsByNombreIgnoreCaseAndEliminadoFalse(String nombre);
}
