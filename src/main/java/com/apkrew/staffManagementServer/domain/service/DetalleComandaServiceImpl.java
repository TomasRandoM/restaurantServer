package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleComanda;
import com.apkrew.staffManagementServer.domain.enums.EstadoDetalleComanda;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleComandaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DetalleComandaServiceImpl extends BaseServiceImpl<DetalleComanda, String> implements DetalleComandaService {

    private final DetalleComandaRepository detalleComandaRepository;

    public DetalleComandaServiceImpl(BaseRepository<DetalleComanda, String> baseRepository, DetalleComandaRepository detalleComandaRepository) {
        super(baseRepository);
        this.detalleComandaRepository = detalleComandaRepository;
    }

    @Override
    @Transactional
    public DetalleComanda cambiarEstado(String id, EstadoDetalleComanda nuevoEstado) throws Exception {
        DetalleComanda detalle = findById(id);
        detalle.setEstadoDetalleComanda(nuevoEstado);
        return repository.save(detalle);
    }

    @Override
    public boolean validar(DetalleComanda entity, String caso) throws ErrorServiceException {
        if (entity.getCantidad() <= 0) {
            throw new ErrorServiceException("La cantidad debe ser mayor a 0");
        }
        String cardId = entity.getDetalleSeccionCarta() != null ? 
                entity.getDetalleSeccionCarta().getId() : 
                entity.getDetalleSeccionCartaId();
        if (cardId == null || cardId.isBlank()) {
            throw new ErrorServiceException("Debe especificar un artículo de la carta");
        }
        return true;
    }
}
