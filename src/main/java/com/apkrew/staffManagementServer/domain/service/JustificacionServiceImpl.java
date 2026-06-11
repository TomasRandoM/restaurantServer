package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Documentacion;
import com.apkrew.staffManagementServer.domain.entity.Justificacion;
import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.domain.enums.TipoJustificacion;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.JustificacionRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Service
public class JustificacionServiceImpl extends BaseServiceImpl<Justificacion, String> implements JustificacionService {

    private final JustificacionRepository justificacionRepository;
    private final DocumentacionServiceImpl documentacionService;
    private final RegistroHorarioServiceImpl registroHorarioService;

    public JustificacionServiceImpl(BaseRepository<Justificacion, String> baseRepository,
                                    JustificacionRepository justificacionRepository,
                                    DocumentacionServiceImpl documentacionService,
                                    RegistroHorarioServiceImpl registroHorarioService) {
        super(baseRepository);
        this.justificacionRepository = justificacionRepository;
        this.documentacionService = documentacionService;
        this.registroHorarioService = registroHorarioService;
    }

    @Transactional
    @Override
    public Justificacion crearJustificacion(String registroHorarioId, TipoJustificacion tipoDocumentacion, String observacion, MultipartFile archivo) throws ErrorServiceException {
        try {
            Justificacion justificacion1 = justificacionRepository.getJustificacionByRegistroHorarioIdAndEliminadoFalse(registroHorarioId);
            Documentacion documentacion = documentacionService.crearDocumentacion(tipoDocumentacion, observacion, archivo);

            RegistroHorario registroHorario = registroHorarioService.findById(registroHorarioId);

            Justificacion justificacion = new Justificacion();
            justificacion.setDocumentacion(documentacion);
            justificacion.setRegistroHorario(registroHorario);
            validar(justificacion, "SAVE");
            justificacion = justificacionRepository.save(justificacion);
            if (justificacion1 != null) {
                justificacion1.setEliminado(true);
                justificacionRepository.save(justificacion1);
            }
            return justificacion;

        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Justificacion crearJustificacionViaUsuario(Date fecha, String employeeId, TipoJustificacion tipoDocumentacion, String observacion, MultipartFile archivo) throws ErrorServiceException {
        try {
            RegistroHorario registroHorario = registroHorarioService.findByFechaAndEmployeeId(fecha, employeeId);
            Justificacion justificacion1 = justificacionRepository.getJustificacionByRegistroHorarioIdAndEliminadoFalse(registroHorario.getId());
            Documentacion documentacion = documentacionService.crearDocumentacion(tipoDocumentacion, observacion, archivo);

            Justificacion justificacion = new Justificacion();
            justificacion.setDocumentacion(documentacion);
            justificacion.setRegistroHorario(registroHorario);
            validar(justificacion, "SAVE");
            justificacion = justificacionRepository.save(justificacion);
            if (justificacion1 != null) {
                justificacion1.setEliminado(true);
                justificacionRepository.save(justificacion1);
            }
            return justificacion;

        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    public Justificacion buscarJustificacionPorRegistroHorario(String registroHorarioId) throws ErrorServiceException {
        try {
            return justificacionRepository.getJustificacionByRegistroHorarioIdAndEliminadoFalse(registroHorarioId);
        } catch (Exception ex) {
            throw new ErrorServiceException("Ocurrió un error al recuperar la justificación");
        }
    }

    @Override
    public boolean validar(Justificacion entity, String caso) throws Exception {
        try {
            if (entity == null) {
                throw new ErrorServiceException("La justificación no puede ser nula");
            }

            if (entity.getRegistroHorario() == null || entity.getRegistroHorario().getId() == null) {
                throw new ErrorServiceException("Debe indicar el registro horario");
            }

            if (entity.getDocumentacion() == null || entity.getDocumentacion().getId() == null) {
                throw new ErrorServiceException("Debe indicar la documentación");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
