package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Pais;
import com.apkrew.staffManagementServer.domain.entity.Provincia;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ProvinciaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaServiceImpl extends BaseServiceImpl<Provincia, String> implements ProvinciaService {

    private final ProvinciaRepository provinciaRepository;

    public ProvinciaServiceImpl(BaseRepository<Provincia, String> baseRepository, ProvinciaRepository provinciaRepository) {
        super(baseRepository);
        this.provinciaRepository = provinciaRepository;
    }

    @Override
    public boolean validar(Provincia entity, String caso) throws ErrorServiceException {
        try {
            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (entity.getPais() == null || entity.getPais().getId() == null) {
                throw new ErrorServiceException("Debe indicar el país");
            }

            entity.setNombre(entity.getNombre().trim());

            if (caso.equals("SAVE")) {

                if (provinciaRepository.existsByNombreAndPaisIdAndEliminadoFalse(
                        entity.getNombre(),
                        entity.getPais().getId())) {

                    throw new ErrorServiceException(
                            "La provincia ya existe para el país seleccionado"
                    );
                }

            } else {

                Provincia provincia = provinciaRepository
                        .findByNombreAndPaisIdAndEliminadoFalse(
                                entity.getNombre(),
                                entity.getPais().getId());

                if (provincia != null &&
                        !provincia.getId().equals(entity.getId())) {

                    throw new ErrorServiceException(
                            "La provincia ya existe para el país seleccionado"
                    );
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
