package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.EmpleadoRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.*;
import com.apkrew.staffManagementServer.domain.enums.TipoImagen;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.EmpleadoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmpleadoServiceImpl extends BaseServiceImpl<Empleado, String> implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioServiceImpl usuarioService;
    private final DireccionServiceImpl direccionService;
    private final ImageServiceImpl imageService;
    private final ContactoCorreoElectronicoService contactoCorreoElectronicoService;
    private final ContactoTelefonicoService contactoTelefonicoService;

    public EmpleadoServiceImpl(BaseRepository<Empleado, String> baseRepository,
                               EmpleadoRepository empleadoRepository,
                               UsuarioServiceImpl usuarioService,
                               DireccionServiceImpl direccionService,
                               ImageServiceImpl imageService, ContactoCorreoElectronicoService contactoCorreoElectronicoService, ContactoTelefonicoService contactoTelefonicoService) {
        super(baseRepository);
        this.empleadoRepository = empleadoRepository;
        this.usuarioService = usuarioService;
        this.direccionService = direccionService;
        this.imageService = imageService;
        this.contactoCorreoElectronicoService = contactoCorreoElectronicoService;
        this.contactoTelefonicoService = contactoTelefonicoService;
    }

    @Override
    @Transactional
    public Empleado crearEmpleado(EmpleadoRequestDTO dto, MultipartFile foto) throws ErrorServiceException {
        try {

            Direccion direccion = direccionService.findById(dto.getDireccionId());

            Imagen imagen = new Imagen();
            imagen.setNombre(foto.getOriginalFilename());
            imagen.setMime(foto.getContentType());
            imagen.setContenido(foto.getBytes());
            imagen.setTipoImagen(TipoImagen.PERSONA);
            imagen = imageService.save(imagen);

            Empleado empleado = new Empleado();
            empleado.setNombre(dto.getNombre());
            empleado.setApellido(dto.getApellido());
            empleado.setTipoDocumentacion(dto.getTipoDocumentacion());
            empleado.setDni(dto.getDni());
            empleado.setFechaNacimiento(dto.getFechaNacimiento());
            empleado.setTipoEmpleado(dto.getTipoEmpleado());
            empleado.setDireccion(direccion);
            empleado.setImagen(imagen);

            ContactoCorreoElectronico contactoCorreoElectronico = new ContactoCorreoElectronico();
            contactoCorreoElectronico.setEmail(dto.getEmail());
            contactoCorreoElectronico.setTipoContacto(dto.getTipoContacto());
            contactoCorreoElectronico.setObservacion(dto.getObservacion());

            ContactoTelefonico contactoTelefonico = new ContactoTelefonico();
            contactoTelefonico.setTelefono(dto.getContactoTelefono());
            contactoTelefonico.setTipoContacto(dto.getTipoContacto());
            contactoTelefonico.setObservacion(dto.getObservacion());
            contactoTelefonico.setTipoTelefono(dto.getTipoTelefono());

            empleado.addContacto(contactoCorreoElectronico);
            empleado.addContacto(contactoTelefonico);

            validar(empleado, "SAVE");

            contactoCorreoElectronico = contactoCorreoElectronicoService.save(contactoCorreoElectronico);
            contactoTelefonico = contactoTelefonicoService.save(contactoTelefonico);

            empleado = empleadoRepository.save(empleado);

            Usuario usuario = new Usuario();
            usuario.setEmail(dto.getEmail());
            usuario.setPassword(dto.getPassword());
            usuario.setRol(dto.getRol());
            usuario.setPersona(empleado);
            usuarioService.save(usuario);

            return empleado;

        } catch (IOException e) {
            throw new ErrorServiceException("Error procesando la imagen.");
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas en empleado");
        }
    }

    @Override
    @Transactional
    public EmpleadoRequestDTO buscarEmpleado(String empleadoId) throws ErrorServiceException {
        try {

            Empleado empleado = findById(empleadoId);
            EmpleadoRequestDTO empleadoDTO = new EmpleadoRequestDTO();

            empleadoDTO.setNombre(empleado.getNombre());
            empleadoDTO.setApellido(empleado.getApellido());
            empleadoDTO.setTipoDocumentacion(empleado.getTipoDocumentacion());
            empleadoDTO.setDni(empleado.getDni());
            empleadoDTO.setFechaNacimiento(empleado.getFechaNacimiento());
            empleadoDTO.setTipoEmpleado(empleado.getTipoEmpleado());
            empleadoDTO.setDireccionId(empleado.getDireccion().getId());

            Usuario usuario = usuarioService.searchByPersona(empleadoId);
            empleadoDTO.setEmail(usuario.getEmail());
            empleadoDTO.setRol(usuario.getRol());

            for (Contacto contacto : empleado.getContacto()) {
                if (contacto instanceof ContactoCorreoElectronico cce) {
                    empleadoDTO.setTipoContacto(cce.getTipoContacto());
                    empleadoDTO.setObservacion(cce.getObservacion());
                } else if (contacto instanceof ContactoTelefonico ct) {
                    empleadoDTO.setContactoTelefono(ct.getTelefono());
                    empleadoDTO.setTipoTelefono(ct.getTipoTelefono());
                }
            }

            return empleadoDTO;

        } catch (IOException e) {
            throw new ErrorServiceException("Error procesando la imagen.");
        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas en empleado");
        }
    }

    @Override
    public boolean validar(Empleado entity, String caso) throws Exception {
        try {
            if (entity == null) {
                throw new ErrorServiceException("El empleado no puede ser nulo");
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

            if (entity.getTipoEmpleado() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de empleado");
            }

            if (entity.getDireccion() == null || entity.getDireccion().getId() == null) {
                throw new ErrorServiceException("Debe indicar la dirección");
            }

            if (entity.getImagen() == null || entity.getImagen().getId() == null) {
                throw new ErrorServiceException("Debe indicar la imagen");
            }

            if (entity.getContacto() == null || entity.getContacto().isEmpty()) {
                throw new ErrorServiceException("Debe indicar al menos un contacto");
            }

            boolean tieneCorreo = false;
            boolean tieneTelefono = false;
            for (Contacto contacto : entity.getContacto()) {
                if (contacto.getTipoContacto() == null) {
                    throw new ErrorServiceException("Debe indicar el tipo de contacto");
                }
                if (contacto instanceof ContactoCorreoElectronico cce) {
                    if (cce.getEmail() == null || cce.getEmail().trim().isEmpty()) {
                        throw new ErrorServiceException("Debe indicar el correo electrónico");
                    }
                    tieneCorreo = true;
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

            if (!tieneCorreo) {
                throw new ErrorServiceException("Debe indicar un correo electrónico");
            }
            if (!tieneTelefono) {
                throw new ErrorServiceException("Debe indicar un teléfono");
            }

            if (caso.equals("SAVE")) {
                if (empleadoRepository.existsByDniAndEliminadoFalse(entity.getDni())) {
                    throw new ErrorServiceException("Ya existe un empleado con ese DNI");
                }
            } else {
                Empleado byDni = empleadoRepository.findByDniAndEliminadoFalse(entity.getDni());
                if (byDni != null && !byDni.getId().equals(entity.getId())) {
                    throw new ErrorServiceException("Ya existe un empleado con ese DNI");
                }
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public boolean existsEmpleadoById(String id) {
        return empleadoRepository.existsByIdAndEliminadoFalse(id);
    }

    @Override
    public Empleado buscarEmpleadoByCorreo(String correo) throws ErrorServiceException {
        try {
            Persona persona = usuarioService.searchByEmail(correo).getPersona();
            if (persona instanceof Empleado) {
                return (Empleado) persona;
            }
            else {
                throw new ErrorServiceException("Error. La persona no es un empleado");
            }
        } catch (Exception e) {
            throw new ErrorServiceException("Error. No se encontró el empleado con el mail asociado");
        }

    }
}
