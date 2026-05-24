package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.ContactoCorreoElectronico;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ContactoCorreoElectronicoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ContactoCorreoElectronicoServiceImpl
        extends BaseServiceImpl<ContactoCorreoElectronico, String>
        implements ContactoCorreoElectronicoService {

    private final ContactoCorreoElectronicoRepository contactoCorreoElectronicoRepository;

    public ContactoCorreoElectronicoServiceImpl(
            BaseRepository<ContactoCorreoElectronico, String> baseRepository,
            ContactoCorreoElectronicoRepository contactoCorreoElectronicoRepository
    ) {
        super(baseRepository);
        this.contactoCorreoElectronicoRepository = contactoCorreoElectronicoRepository;
    }

    @Override
    public boolean validar(ContactoCorreoElectronico entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getEmail() == null ||
                    entity.getEmail().trim().isEmpty()) {

                throw new ErrorServiceException(
                        "Debe indicar el correo electrónico"
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
