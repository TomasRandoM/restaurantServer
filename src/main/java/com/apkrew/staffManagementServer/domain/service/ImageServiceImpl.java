package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Imagen;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;


@Service
public class ImageServiceImpl extends BaseServiceImpl<Imagen, String> implements ImagenService {

    public ImageServiceImpl(BaseRepository<Imagen, String> repository) {
        super(repository);
    }

    @Override
    public boolean validar(Imagen entity, String caso) throws Exception {
        try {
            if (entity == null) {
                throw new ErrorServiceException("La imagen no puede ser nula");
            }

            if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la imagen");
            }

            if (entity.getMime() == null || entity.getMime().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el tipo MIME de la imagen");
            }

            if (entity.getContenido() == null || entity.getContenido().length == 0) {
                throw new ErrorServiceException("La imagen no puede estar vacía");
            }

            if (entity.getTipoImagen() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de imagen");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
