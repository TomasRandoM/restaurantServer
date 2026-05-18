package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Direccion;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DireccionRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DireccionServiceImpl extends BaseServiceImpl<Direccion, String> implements DireccionService {

    private DireccionRepository direccionRepository;

    public DireccionServiceImpl(BaseRepository<Direccion, String> baserepository, DireccionRepository direccionRepository) {
        super(baserepository);
        this.direccionRepository = direccionRepository;
    }

    @Override
    public boolean validar(Direccion entity, String caso) throws Exception {
        try {
            if (entity == null) {
                throw new ErrorServiceException("La dirección no puede ser nula");
            }

            if (entity.getCalle() == null || entity.getCalle().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la calle");
            }

            if (entity.getNumeracion() == null || entity.getNumeracion().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la numeración");
            }

            if (entity.getLocalidad() == null || entity.getLocalidad().getId() == null) {
                throw new ErrorServiceException("Debe indicar la localidad");
            }

            if (caso.equals("SAVE")) {
                if (direccionRepository.existsByCalleAndNumeracionAndLocalidadIdAndEliminadoFalse(
                        entity.getCalle(), entity.getNumeracion(), entity.getLocalidad().getId())) {
                    throw new ErrorServiceException("La dirección ya existe en el sistema");
                }
            } else {
                Direccion cc = direccionRepository.findByCalleAndNumeracionAndLocalidadIdAndEliminadoFalse(
                        entity.getCalle(), entity.getNumeracion(), entity.getLocalidad().getId());
                if (cc != null && !cc.getId().equals(entity.getId())) {
                    throw new ErrorServiceException("La dirección especificada ya existe en el sistema");
                }
            }
            return true;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
