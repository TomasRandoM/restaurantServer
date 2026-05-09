package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Rol;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.RolRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl extends BaseServiceImpl<Rol,String> implements RolService{
    private RolRepository rolRepository;

    public RolServiceImpl(BaseRepository<Rol,String> baseRepository, RolRepository rolRepository){
        super(baseRepository);
        this.rolRepository = rolRepository;
    }

    @Override
    public boolean validar(Rol entity, String caso) throws Exception {

        try {

            if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (caso.equals("SAVE")) {

                if (rolRepository.existsByNombreAndEliminadoFalse(entity.getNombre())) {
                    throw new ErrorServiceException("Ya existe un rol con ese nombre");
                }

            } else {

                Rol rol = rolRepository
                        .findByNombreAndEliminadoFalse(entity.getNombre());

                if (rol != null && !rol.getId().equals(entity.getId())) {
                    throw new ErrorServiceException("Ya existe un rol con ese nombre");
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
