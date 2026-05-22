package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Departamento;
import com.apkrew.staffManagementServer.domain.entity.Localidad;
import com.apkrew.staffManagementServer.domain.entity.Provincia;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.LocalidadRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalidadServiceImpl extends  BaseServiceImpl<Localidad,String> implements LocalidadService{

    private final LocalidadRepository localidadRepository;

    public LocalidadServiceImpl(BaseRepository<Localidad,String> baseRepository, LocalidadRepository localidadRepository){
        super(baseRepository);
        this.localidadRepository = localidadRepository;
    }

    public List<Localidad> findByDepartamento(String departamentoId) throws ErrorServiceException {
        try {
            return localidadRepository.findByDepartamentoIdAndEliminadoFalse(departamentoId);
        } catch (Exception e) {
            throw new ErrorServiceException("Error al buscar localidades por departamento");
        }
    }

    @Override
    public boolean validar(Localidad entity, String caso) throws ErrorServiceException {
        try {
            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (entity.getDepartamento() == null || entity.getDepartamento().getId() == null) {
                throw new ErrorServiceException("Debe indicar el departamento");
            }

            if (entity.getCodigoPostal() == null || entity.getCodigoPostal().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el código postal");
            }

            if (caso.equals("SAVE")) {
                if (localidadRepository.existsByNombreAndDepartamentoIdAndEliminadoFalse(entity.getNombre(), entity.getDepartamento().getId())) {
                    throw new ErrorServiceException("La localidad ingresada ya existe para ese departamento");
                }
            } else {
                Localidad cc = localidadRepository.findByNombreAndDepartamentoIdAndEliminadoFalse(entity.getNombre(), entity.getDepartamento().getId());
                if (cc != null) {
                    if (!cc.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("La localidad especificada ya existe en el sistema");
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
