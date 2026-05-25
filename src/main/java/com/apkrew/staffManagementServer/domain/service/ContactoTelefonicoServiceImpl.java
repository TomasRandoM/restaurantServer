package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.ContactoTelefonico;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ContactoTelefonicoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ContactoTelefonicoServiceImpl
        extends BaseServiceImpl<ContactoTelefonico, String>
        implements ContactoTelefonicoService {

    private final ContactoTelefonicoRepository contactoTelefonicoRepository;

    public ContactoTelefonicoServiceImpl(
            BaseRepository<ContactoTelefonico, String> baseRepository,
            ContactoTelefonicoRepository contactoTelefonicoRepository
    ) {
        super(baseRepository);
        this.contactoTelefonicoRepository = contactoTelefonicoRepository;
    }

    @Override
    public boolean validar(ContactoTelefonico entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getTelefono() == null ||
                    entity.getTelefono().trim().isEmpty()) {

                throw new ErrorServiceException(
                        "Debe indicar el número de teléfono"
                );
            }

            if (entity.getTipoTelefono() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el tipo de teléfono"
                );
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
