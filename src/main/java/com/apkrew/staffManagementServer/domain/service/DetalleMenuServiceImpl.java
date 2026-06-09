package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.DetalleMenu;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DetalleMenuRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DetalleMenuServiceImpl
        extends BaseServiceImpl<DetalleMenu, String>
        implements DetalleMenuService {

    public DetalleMenuServiceImpl(
            BaseRepository<DetalleMenu, String> baserepository,
            DetalleMenuRepository detalleMenuRepository) {

        super(baserepository);
    }

    @Override
    public boolean validar(DetalleMenu entity, String caso)
            throws ErrorServiceException {

        System.out.println(entity.toString());
        try {

            if (entity.getCantidad() <= 0) {

                throw new ErrorServiceException(
                        "La cantidad debe ser mayor a 0");
            }

            if (entity.getMenu() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el menu");
            }

            if (entity.getArticulo() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el articulo");
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
