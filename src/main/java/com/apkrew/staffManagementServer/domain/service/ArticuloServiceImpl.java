package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Articulo;
import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import com.apkrew.staffManagementServer.domain.repository.ArticuloRepository;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo,String> implements ArticuloService {

    private final ArticuloRepository articuloRepository;

    public ArticuloServiceImpl(BaseRepository<Articulo, String> baseRepository, ArticuloRepository articuloRepository) {
        super(baseRepository);
        this.articuloRepository = articuloRepository;
    }


    @Override
    public boolean validar(Articulo entity, String caso) throws ErrorServiceException {
        try {
            if (entity.getNombre() == null || entity.getNombre().isBlank()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (caso.equals("SAVE")) {
                if (articuloRepository.existsByNombreIgnoreCaseAndEliminadoFalse(entity.getNombre())) {
                    throw new ErrorServiceException("El artículo ya existe en el sistema");
                }
            } else {
                Articulo articulo = articuloRepository.findByNombreIgnoreCaseAndEliminadoFalse(entity.getNombre());
                if (articulo != null) {
                    if(!articulo.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("El artículo no existe en el sistema");
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
