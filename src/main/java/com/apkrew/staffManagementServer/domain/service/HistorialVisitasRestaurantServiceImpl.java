package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.HistorialVisitasRestaurant;
import com.apkrew.staffManagementServer.domain.entity.Resenia;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.HistorialVisitasRestaurantRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HistorialVisitasRestaurantServiceImpl extends BaseServiceImpl<HistorialVisitasRestaurant, String> implements HistorialVisitasRestaurantService {
    private final HistorialVisitasRestaurantRepository historialVisitasRestaurantRepository;
    private final ClienteServiceImpl clienteServiceImpl;

    public HistorialVisitasRestaurantServiceImpl(BaseRepository<HistorialVisitasRestaurant, String> baseRepository,
                                                 HistorialVisitasRestaurantRepository historialVisitasRestaurantRepository,
                                                 ClienteServiceImpl clienteServiceImpl) {
        super(baseRepository);
        this.historialVisitasRestaurantRepository = historialVisitasRestaurantRepository;
        this.clienteServiceImpl = clienteServiceImpl;
    }

    @Override
    @Transactional
    public HistorialVisitasRestaurant save(HistorialVisitasRestaurant entity) throws Exception {
        try {
            validar(entity, "SAVE");
            entity.setCliente(clienteServiceImpl.findById(entity.getCliente().getId()));
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
    public HistorialVisitasRestaurant update(String id, HistorialVisitasRestaurant entity) throws Exception {
        try {
            validar(entity, "UPDATE");
            Optional<HistorialVisitasRestaurant> entityOptional = historialVisitasRestaurantRepository.findByIdAndEliminadoFalse(id);
            HistorialVisitasRestaurant entityUpdate = entityOptional.get();
            entity.setId(entityUpdate.getId());
            entity.setCliente(clienteServiceImpl.findById(entity.getCliente().getId()));
            entityUpdate = repository.save(entity);
            return entityUpdate;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("La entidad con el id ingresado no existe");
        }
    }

    @Override
    public boolean validar(HistorialVisitasRestaurant entity, String caso) throws Exception {
        if(clienteServiceImpl.findById(entity.getCliente().getId()) == null) {
            throw new ErrorServiceException("Cliente no encontrado");
        }
        if(entity.getCantidadVisita() < 0) {
            throw new ErrorServiceException("Cantidad de visitas negativo");
        }
        if(caso.equals("SAVE")) {
            if(historialVisitasRestaurantRepository.findByClienteAndEliminadoFalse(entity.getCliente()).isPresent()) {
                throw new ErrorServiceException("Ya existe una historial visita con el cliente");
            }
        } else if(caso.equals("UPDATE")) {
            if(historialVisitasRestaurantRepository.findByClienteAndEliminadoFalse(entity.getCliente()).isEmpty()) {
                throw new ErrorServiceException("El historial de visita no existe");
            }
        }
        return true;
    }
}
