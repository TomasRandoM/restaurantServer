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

    public EmpleadoServiceImpl(BaseRepository<Empleado, String> baseRepository,
                               EmpleadoRepository empleadoRepository,
                               UsuarioServiceImpl usuarioService,
                               DireccionServiceImpl direccionService,
                               ImageServiceImpl imageService) {
        super(baseRepository);
        this.empleadoRepository = empleadoRepository;
        this.usuarioService = usuarioService;
        this.direccionService = direccionService;
        this.imageService = imageService;
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

            validar(empleado, "SAVE");
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
}
