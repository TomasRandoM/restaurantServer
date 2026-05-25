package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Stock;
import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import com.apkrew.staffManagementServer.domain.repository.ArticuloRepository;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.StockRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockServiceImpl extends BaseServiceImpl<Stock, String> implements StockService {
    private final StockRepository stockRepository;
    private final ArticuloRepository articuloRepository;

    public StockServiceImpl(BaseRepository<Stock, String> baseRepository, StockRepository stockRepository, ArticuloRepository articuloRepository) {
        super(baseRepository);
        this.stockRepository = stockRepository;
        this.articuloRepository = articuloRepository;
    }

    @Override
    public boolean validar(Stock entity, String caso) throws ErrorServiceException {
        try {
            if (entity.getArticulo().getId() == null || entity.getArticulo().getId().isBlank()) {
                throw new ErrorServiceException("No hay articulo seleccionado");
            }

            if (caso.equals("SAVE")) {
                if (!articuloRepository.existsById(entity.getArticulo().getId())) {
                    throw new ErrorServiceException("El artículo no existe");
                }
                if (stockRepository.existsByArticuloIdAndEliminadoFalse(entity.getArticulo().getId())) {
                    throw new ErrorServiceException("Ya hay stock para el artículo");
                }
            } else {
                Optional<Stock> stockOptional = stockRepository.findByArticulo(entity.getArticulo());
                if (stockOptional.isPresent()) {
                    if (!stockOptional.get().getId().equals(entity.getId())) {
                        throw new ErrorServiceException("El stock no existe en el sistema");
                    }
                }
            }

            return true;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");

        }
    }
}
