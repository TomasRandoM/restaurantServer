package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Departamento;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DepartamentoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServiceImpl
        extends BaseServiceImpl<Departamento, String>
        implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(
            BaseRepository<Departamento, String> baseRepository,
            DepartamentoRepository departamentoRepository
    ) {
        super(baseRepository);
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public boolean validar(Departamento entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getNombre() == null ||
                    entity.getNombre().trim().isEmpty()) {

                throw new ErrorServiceException(
                        "Debe indicar el nombre del departamento"
                );
            }

            if (entity.getProvincia() == null ||
                    entity.getProvincia().getId() == null) {

                throw new ErrorServiceException(
                        "Debe indicar la provincia"
                );
            }

            entity.setNombre(entity.getNombre().trim().toUpperCase());

            if (caso.equals("SAVE")) {

                if (departamentoRepository
                        .existsByNombreAndProvinciaIdAndEliminadoFalse(
                                entity.getNombre(),
                                entity.getProvincia().getId()
                        )) {

                    throw new ErrorServiceException(
                            "El departamento ya existe para la provincia seleccionada"
                    );
                }

            } else {

                Departamento departamento =
                        departamentoRepository
                                .findByNombreAndProvinciaIdAndEliminadoFalse(
                                        entity.getNombre(),
                                        entity.getProvincia().getId()
                                );

                if (departamento != null &&
                        !departamento.getId().equals(entity.getId())) {

                    throw new ErrorServiceException(
                            "El departamento ya existe para la provincia seleccionada"
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

    public List<Departamento> findByProvincia(String provinciaId) throws Exception {
        try {
            return departamentoRepository.findByProvinciaIdAndEliminadoFalse(provinciaId);
        } catch (Exception e) {
            throw new ErrorServiceException("Error al buscar los departamentos");
        }
    }
}
