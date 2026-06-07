package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Cliente;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ClienteRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, String> implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(BaseRepository<Cliente, String> baseRepository, ClienteRepository clienteRepository) {
        super(baseRepository);
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean validar(Cliente entity, String caso) throws Exception {
        try {
            if(entity.getNombre() == null || entity.getNombre().isBlank()) {
                throw new ErrorServiceException("Debe indicar el nombre del cliente");
            }
            if(entity.getApellido() == null || entity.getApellido().isBlank()) {
                throw new ErrorServiceException("Debe indicar el apellido del cliente");
            }
            if(entity.getTipoDocumentacion() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de documentación del cliente");
            }
            if(entity.getDni() == null || entity.getDni().isBlank()) {
                throw new ErrorServiceException("Debe indicar el dni del cliente");
            }
            if(entity.getFechaNacimiento() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de nacimiento del cliente");
            }
            if((entity.getDireccionEstadia() == null || entity.getDireccionEstadia().isBlank()) && entity.getDireccion() == null) {
                throw new ErrorServiceException("Debe indicar la dirección del cliente");
            }
            return true;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException(ex.getMessage());
        }
    }
}
