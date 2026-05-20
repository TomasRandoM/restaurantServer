package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Promocion;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends BaseRepository<Promocion, String> {

    boolean existsByDescripcionAndEliminadoFalse(String descripcion);

    Promocion findByDescripcionAndEliminadoFalse(String descripcion);

}
