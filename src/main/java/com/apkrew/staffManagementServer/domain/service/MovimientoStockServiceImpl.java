package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.MovimientoStock;
import com.apkrew.staffManagementServer.domain.entity.Stock;
import com.apkrew.staffManagementServer.domain.enums.TipoMovimientoStock;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.MovimientoStockRepository;
import com.apkrew.staffManagementServer.domain.repository.StockRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovimientoStockServiceImpl extends BaseServiceImpl<MovimientoStock, String> {
    private final MovimientoStockRepository movimientoStockRepository;
    private final StockRepository stockRepository;
    private final StockServiceImpl stockService;

    public MovimientoStockServiceImpl(BaseRepository<MovimientoStock, String>baseRepository,
                                      MovimientoStockRepository movimientoStockRepository,
                                      StockServiceImpl stockService,
                                      StockRepository stockRepository) {
        super(baseRepository);
        this.movimientoStockRepository = movimientoStockRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional
    public MovimientoStock save(MovimientoStock entity) throws Exception {
        try {
            validar(entity, "SAVE");
            Stock stock = stockService.findById(entity.getStock().getId());
            if (entity.getTipoMovimiento() == TipoMovimientoStock.ENTRADA) {
                stock.setCantidadActual(
                        stock.getCantidadActual() + entity.getCantidad()
                );
            } else {
                stock.setCantidadActual(
                        stock.getCantidadActual() - entity.getCantidad()
                );
            }
            stockService.update(stock.getId(), stock);
            entity = repository.save(entity);
            return entity;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al guardar la entidad");
        }
    }

    @Override
    @Transactional
    public MovimientoStock update(String id, MovimientoStock entity) throws Exception {
        try {
            validar(entity, "UPDATE");
            Stock stock = stockService.findById(entity.getStock().getId());
            MovimientoStock entityUpdate = movimientoStockRepository.findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException("Movimiento de stock no encontrado"));
            if (entity.getTipoMovimiento() == entityUpdate.getTipoMovimiento()) {
                double diferencia = entity.getCantidad() - entityUpdate.getCantidad();
                if(entity.getTipoMovimiento() == TipoMovimientoStock.ENTRADA) {
                    stock.setCantidadActual(stock.getCantidadActual() + diferencia);
                } else {
                    stock.setCantidadActual(stock.getCantidadActual() - diferencia);
                }
            } else {
                if (entityUpdate.getTipoMovimiento() == TipoMovimientoStock.ENTRADA) {
                    stock.setCantidadActual(stock.getCantidadActual() - entityUpdate.getCantidad() - entity.getCantidad());
                } else {
                    stock.setCantidadActual(stock.getCantidadActual() + entityUpdate.getCantidad() + entity.getCantidad());
                }
            }
            stockService.update(stock.getId(), stock);
            entity.setId(id);
            entityUpdate = repository.save(entity);
            return entityUpdate;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("Error al guardar la entidad");
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) throws Exception {
        try {
            if (repository.existsByIdAndEliminadoFalse(id)) {
                MovimientoStock entity = movimientoStockRepository.findByIdAndEliminadoFalse(id)
                                .orElseThrow(() -> new ErrorServiceException("Movimiento de stock no encontrado"));
                Stock stock = stockService.findById(entity.getStock().getId());
                entity.setEliminado(true);
                if(entity.getTipoMovimiento() == TipoMovimientoStock.ENTRADA) {
                    stock.setCantidadActual(stock.getCantidadActual() - entity.getCantidad());
                } else {
                    stock.setCantidadActual(stock.getCantidadActual() + entity.getCantidad());
                }
                stockService.update(stock.getId(), stock);
                repository.save(entity);
                return true;
            } else {
                throw new ErrorServiceException("El objeto ya fue eliminado o no existe");
            }
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception("Hubo un problema eliminando el objeto");
        }
    }

    @Override
    public boolean validar(MovimientoStock entity, String caso) throws Exception {
        try {
            Stock stock = stockService.findById(entity.getStock().getId());
            if (entity.getCantidad() <= 0) {
                throw new ErrorServiceException("La cantidad debe ser mayor a 0");
            }
            if(entity.getTipoMovimiento() == TipoMovimientoStock.SALIDA) {
                if (stock.getCantidadActual() - entity.getCantidad() < 0 ) {
                    throw new ErrorServiceException("La cantidad solicitada del producto es mayor a la disponible");
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
