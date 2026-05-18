package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Documentacion;
import com.apkrew.staffManagementServer.domain.entity.Justificacion;
import com.apkrew.staffManagementServer.domain.entity.RegistroHorario;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.JustificacionRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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

    @Override
    public Justificacion crearJustificacion(String registroHorarioId, TipoDocumentacion tipoDocumentacion, String observacion, MultipartFile archivo) throws ErrorServiceException {
        try {
            Documentacion documentacion = documentacionService.crearDocumentacion(tipoDocumentacion, observacion, archivo);

            RegistroHorario registroHorario = registroHorarioService.findById(registroHorarioId);

            Justificacion justificacion = new Justificacion();
            justificacion.setDocumentacion(documentacion);
            justificacion.setRegistroHorario(registroHorario);
            validar(justificacion, "SAVE");
            return justificacionRepository.save(justificacion);

        } catch (ErrorServiceException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
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
