package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleSeccionCartaArticuloIndividual;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleSeccionCartaArticuloIndividualRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DetalleSeccionCartaArticuloIndividualServiceImpl
        extends BaseServiceImpl<DetalleSeccionCartaArticuloIndividual, String>
        implements DetalleSeccionCartaArticuloIndividualService {

    private final DetalleSeccionCartaArticuloIndividualRepository repository;

    public DetalleSeccionCartaArticuloIndividualServiceImpl(
            BaseRepository<DetalleSeccionCartaArticuloIndividual, String> baseRepository,
            DetalleSeccionCartaArticuloIndividualRepository repository) {

        super(baseRepository);
        this.repository = repository;
    }

    @Override
    public boolean validar(
            DetalleSeccionCartaArticuloIndividual entity,
            String caso) throws ErrorServiceException {

        try {

            if (entity.getArticulo() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el artículo");
            }

            if (entity.getSeccionCarta() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la sección");
            }

            if (entity.getPrecio() <= 0) {
                throw new ErrorServiceException(
                        "El precio debe ser mayor a cero");
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
