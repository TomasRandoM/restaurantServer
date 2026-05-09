package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Domicilio;
import com.apkrew.staffManagementServer.domain.entity.Pais;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DomicilioRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class DomicilioServiceImpl extends BaseServiceImpl<Domicilio, String> implements DomicilioService {

    private DomicilioRepository domicilioRepository;

    public DomicilioServiceImpl(BaseRepository<Domicilio, String> baserepository, DomicilioRepository domicilioRepository) {
        super(baserepository);
        this.domicilioRepository = domicilioRepository;
    }

    @Override
    public boolean validar(Domicilio entity, String caso) throws Exception {
        try {
            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (entity.getCp() == null || entity.getCp().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el código postal");
            }

            if (entity.getNumero() == null || entity.getNumero().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el número");
            }

            if (entity.getLocalidad() == null) {
                throw new ErrorServiceException("Debe indicar la localidad");
            }

            if (caso.equals("SAVE")) {
                if (domicilioRepository.existsByNombreAndNumeroAndCpAndLocalidadIdAndEliminadoFalse(entity.getNombre(),
                        entity.getNumero(), entity.getCp(), entity.getLocalidad().getId())) {
                    throw new ErrorServiceException("El domicilio ya existe en el sistema");
                }
            } else {
                Domicilio cc = domicilioRepository.findByNombreAndNumeroAndCpAndLocalidadIdAndEliminadoFalse(entity.getNombre(),
                        entity.getNumero(), entity.getCp(), entity.getLocalidad().getId());
                if (cc != null) {
                    if (!cc.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("El domicilio especificado ya existe en el sistema");
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
