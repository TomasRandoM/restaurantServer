package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ReseniaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.apkrew.staffManagementServer.domain.entity.Resenia;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReseniaServiceImpl extends BaseServiceImpl<Resenia, String> implements ReseniaService {

    private final ReseniaRepository reseniaRepository;
    private final ClienteServiceImpl clienteServiceImpl;

    public ReseniaServiceImpl(BaseRepository<Resenia, String> baseRepository,
                              ReseniaRepository reseniaRepository,
                              ClienteServiceImpl clienteServiceImpl) {
        super(baseRepository);
        this.reseniaRepository = reseniaRepository;
        this.clienteServiceImpl = clienteServiceImpl;
    }

    @Override
    @Transactional
    public Resenia save(Resenia entity) throws Exception {
        try {
            validar(entity, "SAVE");
            entity.setCliente(clienteServiceImpl.findById(entity.getCliente().getId()));
            entity.setFechaResenia(LocalDateTime.now());
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
    public Resenia update(String id, Resenia entity) throws Exception {
        try {
            validar(entity, "UPDATE");
            Optional<Resenia> entityOptional = reseniaRepository.findByIdAndEliminadoFalse(id);
            Resenia entityUpdate = entityOptional.get();
            entity.setId(entityUpdate.getId());
            entity.setCliente(clienteServiceImpl.findById(entity.getCliente().getId()));
            entity.setFechaResenia(LocalDateTime.now());
            entityUpdate = repository.save(entity);
            return entityUpdate;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ErrorServiceException("La entidad con el id ingresado no existe");
        }
    }

    @Override
    public boolean validar(Resenia entity, String caso) throws Exception {
        if(clienteServiceImpl.findById(entity.getCliente().getId()) == null) {
            throw new ErrorServiceException("Cliente no encontrado");
        }
        if(entity.getObservacion() != null && entity.getObservacion().isBlank()) {
            throw new ErrorServiceException("Observacion no válida");
        }
        return true;
    }
}
