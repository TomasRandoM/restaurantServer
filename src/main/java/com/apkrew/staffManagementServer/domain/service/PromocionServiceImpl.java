package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Promocion;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.PromocionRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class PromocionServiceImpl extends BaseServiceImpl<Promocion, String>
        implements PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionServiceImpl(
            BaseRepository<Promocion, String> baserepository,
            PromocionRepository promocionRepository) {

        super(baserepository);
        this.promocionRepository = promocionRepository;
    }

    @Override
    public boolean validar(Promocion entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getDescripcion() == null ||
                    entity.getDescripcion().trim().isEmpty()) {

                throw new ErrorServiceException("Debe indicar la descripcion");
            }

            entity.setDescripcion(entity.getDescripcion().trim());

            if (entity.getPorcentajeDescuento() <= 0 ||
                    entity.getPorcentajeDescuento() > 100) {

                throw new ErrorServiceException(
                        "El porcentaje de descuento debe estar entre 1 y 100");
            }

            if (caso.equals("SAVE")) {

                if (promocionRepository
                        .existsByDescripcionAndEliminadoFalse(
                                entity.getDescripcion())) {

                    throw new ErrorServiceException(
                            "La promocion ya existe en el sistema");
                }

            } else {

                Promocion promocion = promocionRepository
                        .findByDescripcionAndEliminadoFalse(
                                entity.getDescripcion());

                if (promocion != null) {

                    if (!promocion.getId().equals(entity.getId())) {

                        throw new ErrorServiceException(
                                "La promocion especificada ya existe en el sistema");
                    }
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