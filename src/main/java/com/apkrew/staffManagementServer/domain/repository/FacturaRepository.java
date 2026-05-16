package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Factura;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository
        extends BaseRepository<Factura, String> {

    boolean existsByNumeroFacturaAndEliminadoFalse(Long numeroFactura);

    Factura findByNumeroFacturaAndEliminadoFalse(Long numeroFactura);

}
