package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Articulo;
import com.apkrew.staffManagementServer.domain.entity.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends BaseRepository<Stock, String> {
    Optional<Stock> findByArticuloAndEliminadoFalse(Articulo articulo);
    boolean existsByArticuloIdAndEliminadoFalse(String articuloId);
    @Query("""
        SELECT COUNT(s)
        FROM Stock s
        WHERE s.cantidadActual < s.minimo
    """)
    int countStockCritico();
}
