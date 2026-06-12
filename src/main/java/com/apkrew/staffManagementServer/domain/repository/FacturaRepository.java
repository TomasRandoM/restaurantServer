package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository
        extends BaseRepository<Factura, String> {

    @Query("SELECT COALESCE(MAX(f.numeroFactura), 0) FROM Factura f WHERE f.eliminado = false")
    Long findMaxNumeroFactura();

}
