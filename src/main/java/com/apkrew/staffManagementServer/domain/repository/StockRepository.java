package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.Articulo;
import com.apkrew.staffManagementServer.domain.entity.Stock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends BaseRepository<Stock, String> {
    Optional<Stock> findByArticulo(Articulo articulo);
    boolean existsByArticuloIdAndEliminadoFalse(String articuloId);
}
