package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleSeccionCartaMenu;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleSeccionCartaMenuRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DetalleSeccionCartaMenuServiceImpl
        extends BaseServiceImpl<DetalleSeccionCartaMenu, String>
        implements DetalleSeccionCartaMenuService {

    private final DetalleSeccionCartaMenuRepository repository;

    public DetalleSeccionCartaMenuServiceImpl(
            BaseRepository<DetalleSeccionCartaMenu, String> baseRepository,
            DetalleSeccionCartaMenuRepository repository) {

        super(baseRepository);
        this.repository = repository;
    }

    @Override
    public boolean validar(
            DetalleSeccionCartaMenu entity,
            String caso) throws ErrorServiceException {

        try {

            if (entity.getSeccionCarta() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la sección");
            }

            if (entity.getMenus() == null ||
                    entity.getMenus().isEmpty()) {

                throw new ErrorServiceException(
                        "Debe indicar al menos un menú");
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
