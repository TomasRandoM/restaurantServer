package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.ItemReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ItemReciboDeSueldoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ItemReciboDeSueldoServiceImpl
        extends BaseServiceImpl<ItemReciboDeSueldo, String>
        implements ItemReciboDeSueldoService {

    private final ItemReciboDeSueldoRepository itemReciboDeSueldoRepository;

    public ItemReciboDeSueldoServiceImpl(
            BaseRepository<ItemReciboDeSueldo, String> baseRepository,
            ItemReciboDeSueldoRepository itemReciboDeSueldoRepository) {

        super(baseRepository);
        this.itemReciboDeSueldoRepository = itemReciboDeSueldoRepository;
    }

    @Override
    public boolean validar(ItemReciboDeSueldo entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getNombre() == null || entity.getNombre().isBlank()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (caso.equals("SAVE")) {
                if (itemReciboDeSueldoRepository
                        .existsByNombreIgnoreCaseAndEliminadoFalse(
                                entity.getNombre())) {
                    throw new ErrorServiceException(
                            "El item de recibo de sueldo ya existe en el sistema");
                }
            } else {
                ItemReciboDeSueldo item =
                        itemReciboDeSueldoRepository
                                .findByNombreIgnoreCaseAndEliminadoFalse(
                                        entity.getNombre());

                if (item != null) {
                    if (!item.getId().equals(entity.getId())) {
                        throw new ErrorServiceException(
                                "El item de recibo de sueldo especificado ya existe en el sistema");
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
