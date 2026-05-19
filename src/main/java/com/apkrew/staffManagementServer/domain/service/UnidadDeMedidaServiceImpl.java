package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.UnidadDeMedidaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class UnidadDeMedidaServiceImpl extends BaseServiceImpl<UnidadDeMedida, String> implements UnidadDeMedidaService {

    private final UnidadDeMedidaRepository unidadDeMedidaRepository;

    public UnidadDeMedidaServiceImpl(BaseRepository<UnidadDeMedida, String> baseRepository, UnidadDeMedidaRepository unidadRepository) {
        super(baseRepository);
        this.unidadDeMedidaRepository = unidadRepository;
    }

    @Override
    public boolean validar(UnidadDeMedida entity, String caso) throws ErrorServiceException {
        try {
            if (entity.getNombre() == null || entity.getNombre().isBlank()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (caso.equals("SAVE")) {
                if (unidadDeMedidaRepository.existsByNombreIgnoreCaseAndEliminadoFalse(entity.getNombre())) {
                    throw new ErrorServiceException("La unidad de medida ya existe en el sistema");
                }
            } else {
                UnidadDeMedida unidadDeMedida = unidadDeMedidaRepository.findByNombreIgnoreCaseAndEliminadoFalse(entity.getNombre());
                if (unidadDeMedida != null) {
                    if(!unidadDeMedida.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("La unidad de medida no existe en el sistema");
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
