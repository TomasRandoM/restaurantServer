package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.SeccionCarta;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.SeccionCartaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class SeccionCartaServiceImpl
        extends BaseServiceImpl<SeccionCarta, String>
        implements SeccionCartaService {

    private final SeccionCartaRepository seccionCartaRepository;

    public SeccionCartaServiceImpl(
            BaseRepository<SeccionCarta, String> baserepository,
            SeccionCartaRepository seccionCartaRepository) {

        super(baserepository);
        this.seccionCartaRepository = seccionCartaRepository;
    }

    @Override
    public boolean validar(SeccionCarta entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getCarta() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la carta");
            }

            if (entity.getCategoria() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la categoría");
            }

            if (caso.equals("SAVE")) {

                if (seccionCartaRepository
                        .existsByCartaIdAndCategoriaIdAndEliminadoFalse(
                                entity.getCarta().getId(),
                                entity.getCategoria().getId())) {

                    throw new ErrorServiceException(
                            "La categoría ya se encuentra asociada a la carta");
                }

            } else {

                SeccionCarta sc =
                        seccionCartaRepository
                                .findByCartaIdAndCategoriaIdAndEliminadoFalse(
                                        entity.getCarta().getId(),
                                        entity.getCategoria().getId());

                if (sc != null &&
                        !sc.getId().equals(entity.getId())) {

                    throw new ErrorServiceException(
                            "La categoría ya se encuentra asociada a la carta");
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