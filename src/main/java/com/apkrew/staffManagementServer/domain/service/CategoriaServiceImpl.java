package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Categoria;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.CategoriaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl extends BaseServiceImpl<Categoria, String> implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(BaseRepository<Categoria, String> baserepository,
                                CategoriaRepository categoriaRepository) {
        super(baserepository);
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public boolean validar(Categoria entity, String caso) throws ErrorServiceException {
        try {

            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (caso.equals("SAVE")) {

                if (categoriaRepository.existsByNombreAndEliminadoFalse(entity.getNombre())) {
                    throw new ErrorServiceException("La categoría ya existe en el sistema");
                }

            } else {

                Categoria cc =
                        categoriaRepository.findByNombreAndEliminadoFalse(entity.getNombre());

                if (cc != null) {

                    if (!cc.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("La categoría especificada ya existe en el sistema");
                    }

                }
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
