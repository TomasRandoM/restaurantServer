package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Documentacion;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.domain.enums.TipoJustificacion;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.DocumentacionRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentacionServiceImpl extends BaseServiceImpl<Documentacion, String> implements DocumentacionService{

    @Autowired
    private DocumentacionRepository documentacionRepository;

    @Value("${app.storage.documentacion:files/documentacion/}")
    private String ruta;

    public DocumentacionServiceImpl(BaseRepository<Documentacion, String> repository) {
        super(repository);
    }

    @Override
    public boolean validar(Documentacion documentacion, String caso) throws Exception {
        try {
            if (documentacion == null) {
                throw new ErrorServiceException("La documentación no puede ser nula");
            }

            if (documentacion.getTipoDocumentacion() == null) {
                throw new ErrorServiceException("Debe indicar el tipo de documentación");
            }

            if (documentacion.getNombreArchivo() == null || documentacion.getNombreArchivo().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre del archivo");
            }

            if (documentacion.getPathArchivo() == null || documentacion.getPathArchivo().trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la ruta del archivo");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Escribe el archivo en el almacenamiento.
     * @param tipoDocumentacion TipoDocumentacion
     * @param observacion String
     * @param archivo MultipartFile, puede ser foto o pdf.
     * @return Documentacion
     * @throws ErrorServiceException
     */
    public Documentacion crearDocumentacion(TipoJustificacion tipoDocumentacion, String observacion, MultipartFile archivo) throws ErrorServiceException {
        try {
            Documentacion documentacion = new Documentacion();
            String nombreOriginal = archivo.getOriginalFilename();
            documentacion.setNombreArchivo(nombreOriginal);
            String nuevoNombre = UUID.randomUUID().toString() + nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            Path destino = Paths.get(ruta, nuevoNombre);
            documentacion.setNombreArchivo(nombreOriginal);
            documentacion.setTipoDocumentacion(tipoDocumentacion);
            documentacion.setObservacion(observacion);
            documentacion.setPathArchivo(destino.toString());
            validar(documentacion, "SAVE");
            Files.createDirectories(destino.getParent());
            Files.write(destino, archivo.getBytes());
            return documentacionRepository.save(documentacion);

        } catch (IOException e) {
            throw new ErrorServiceException("Error guardando el documento.");
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Modifica un archivo existente en el storage. Guarda la nueva version manteniendo ambas
     * @param documentacion Documentacion
     * @param archivo MultipartFile
     * @return Documentacion
     * @throws ErrorServiceException
     */
    public Documentacion modificarDocumentacion(Documentacion documentacion, MultipartFile archivo) throws ErrorServiceException {
        try {
            String nombreOriginal = archivo.getOriginalFilename();
            documentacion.setNombreArchivo(nombreOriginal);
            String nuevoNombre = UUID.randomUUID().toString() + nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            Path destino = Paths.get(ruta, nuevoNombre);
            documentacion.setNombreArchivo(nombreOriginal);
            documentacion.setPathArchivo(destino.toString());
            validar(documentacion, "UPDATE");
            Files.createDirectories(destino.getParent());
            Files.write(destino, archivo.getBytes());
            return documentacionRepository.save(documentacion);

        } catch (IOException e) {
            throw new ErrorServiceException("Error guardando el documento.");
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca la documentacion (el archivo) y lo devuelve como byte[]
     * @param id String
     * @return byte[]
     * @throws ErrorServiceException
     */
    public byte[] buscarDocumentacion(String id) throws ErrorServiceException {
        try {
            Optional<Documentacion> documentacion = documentacionRepository.findByIdAndEliminadoFalse(id);
            Documentacion documentacion1;
            if (documentacion.isPresent()) {
                documentacion1 = documentacion.get();
            }
            else {
                throw new ErrorServiceException("La documentación no se encuentra cargada en el sistema");
            }

            return Files.readAllBytes(Paths.get(documentacion1.getPathArchivo()));
        } catch (IOException ex) {
            throw new ErrorServiceException("Error leyendo la documentación");
        } catch (ErrorServiceException ex) {
            throw ex;
        }

    }
}
