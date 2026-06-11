package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Empresa;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.EmpresaRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa,String> implements EmpresaService{

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(BaseRepository<Empresa, String> baseRepository , EmpresaRepository empresaRepository) {
        super(baseRepository);
        this.empresaRepository = empresaRepository;
    }

    @Override
    public boolean validar(Empresa entity, String caso) throws ErrorServiceException {
        try {

            if (entity == null) {
                throw new ErrorServiceException("Debe indicar la empresa");
            }

            if (entity.getNombre() != null) {
                entity.setNombre(entity.getNombre().trim());
            }

            if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre de la empresa");
            }

            if (entity.getNombre().length() > 255) {
                throw new ErrorServiceException(
                        "El nombre no puede superar los 255 caracteres"
                );
            }

            if (entity.getTelefono() != null) {

                entity.setTelefono(entity.getTelefono().trim());

                if (entity.getTelefono().isEmpty()) {
                    entity.setTelefono(null);
                }
            }

            if (entity.getCorreoElectronico() != null) {

                entity.setCorreoElectronico(entity.getCorreoElectronico().trim());

                if (entity.getCorreoElectronico().isEmpty()) {
                    entity.setCorreoElectronico(null);
                }
            }

            if (caso.equals("SAVE")) {

                if (empresaRepository
                        .existsByNombreAndEliminadoFalse(entity.getNombre())) {

                    throw new ErrorServiceException(
                            "La empresa ya existe en el sistema"
                    );
                }

            } else {

                Empresa empresa = empresaRepository
                        .findByNombreAndEliminadoFalse(entity.getNombre());

                if (empresa != null) {

                    if (!empresa.getId().equals(entity.getId())) {

                        throw new ErrorServiceException(
                                "La empresa especificada ya existe en el sistema"
                        );
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
