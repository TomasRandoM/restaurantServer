package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ClienteRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.EmpleadoRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.enums.TipoImagen;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ClienteRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, String> implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final DireccionServiceImpl direccionService;
    private final ContactoTelefonicoServiceImpl contactoTelefonicoService;

    public ClienteServiceImpl(BaseRepository<Cliente, String> baseRepository,
                              ClienteRepository clienteRepository,
                              DireccionServiceImpl direccionService,
                              ContactoTelefonicoServiceImpl contactoTelefonicoService) {
        super(baseRepository);
        this.clienteRepository = clienteRepository;
        this.direccionService = direccionService;
        this.contactoTelefonicoService = contactoTelefonicoService;
    }

    @Transactional
    public Cliente crearCliente(ClienteRequestDTO dto) throws ErrorServiceException {
        try {
            Direccion direccion = null;
            if(dto.getDireccionId() != null && !dto.getDireccionId().isBlank()) {
                direccion = direccionService.findById(dto.getDireccionId());
            }


            Cliente cliente = new Cliente();
            cliente.setNombre(dto.getNombre());
            cliente.setApellido(dto.getApellido());
            cliente.setTipoDocumentacion(dto.getTipoDocumentacion());
            cliente.setDni(dto.getDni());
            cliente.setFechaNacimiento(dto.getFechaNacimiento());
            cliente.setDireccion(direccion);
            cliente.setDireccionEstadia(dto.getDireccionEstadia());

            ContactoTelefonico contactoTelefonico = new ContactoTelefonico();
            contactoTelefonico.setTelefono(dto.getContactoTelefono());
            contactoTelefonico.setTipoContacto(dto.getTipoContacto());
            contactoTelefonico.setObservacion(dto.getObservacion());
            contactoTelefonico.setTipoTelefono(dto.getTipoTelefono());

            cliente.addContacto(contactoTelefonico);

            validar(cliente, "SAVE");

            contactoTelefonico = contactoTelefonicoService.save(contactoTelefonico);

            cliente = clienteRepository.save(cliente);

            return cliente;

        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas en cliente");
        }
    }

    @Override
    public boolean validar(Cliente entity, String caso) throws Exception {
        try {
            if (entity == null) {
                throw new ErrorServiceException("El cliente no puede ser nulo");
            }

            if (entity.getNombre() == null || entity.getNombre().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (entity.getApellido() == null || entity.getApellido().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el apellido");
            }

            if (entity.getTipoDocumentacion() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de documentación");
            }

            if (entity.getDni() == null || entity.getDni().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el DNI");
            }

            if (entity.getFechaNacimiento() == null) {
                throw new ErrorServiceException("Debe indicar la fecha de nacimiento");
            }

            if ((entity.getDireccion() == null || entity.getDireccion().getId() == null) &&
                    (entity.getDireccionEstadia() == null || entity.getDireccionEstadia().isBlank())) {
                throw new ErrorServiceException("Debe indicar la dirección");
            }

            if (entity.getContacto() == null || entity.getContacto().isEmpty()) {
                throw new ErrorServiceException("Debe indicar al menos un contacto");
            }

            boolean tieneTelefono = false;
            for (Contacto contacto : entity.getContacto()) {
                if (contacto.getTipoContacto() == null) {
                    throw new ErrorServiceException("Debe indicar el tipo de contacto");
                } else if (contacto instanceof ContactoTelefonico ct) {
                    if (ct.getTelefono() == null || ct.getTelefono().trim().isEmpty()) {
                        throw new ErrorServiceException("Debe indicar el número de teléfono");
                    }
                    if (ct.getTipoTelefono() == null) {
                        throw new ErrorServiceException("Debe indicar el tipo de teléfono");
                    }
                    tieneTelefono = true;
                }
            }

            if (!tieneTelefono) {
                throw new ErrorServiceException("Debe indicar un teléfono");
            }

            if (caso.equals("SAVE")) {
                if (clienteRepository.existsByDniAndEliminadoFalse(entity.getDni())) {
                    throw new ErrorServiceException("Ya existe un cliente con ese DNI");
                }
            } else {
                Cliente byDni = clienteRepository.findByDniAndEliminadoFalse(entity.getDni());
                if (byDni != null && !byDni.getId().equals(entity.getId())) {
                    throw new ErrorServiceException("Ya existe un cliente con ese DNI");
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
