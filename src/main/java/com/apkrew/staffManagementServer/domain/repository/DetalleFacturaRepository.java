package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.DetalleFactura;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepository
        extends BaseRepository<DetalleFactura, String> {
}
