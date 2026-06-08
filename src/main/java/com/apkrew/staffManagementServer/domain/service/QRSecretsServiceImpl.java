package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.QRSecrets;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.QRSecretsRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QRSecretsServiceImpl implements QRSecretsService {

    QRSecretsRepository qrSecretsRepository;
    EmpleadoService empleadoService;
    public QRSecretsServiceImpl(QRSecretsRepository qrSecretsRepository, EmpleadoService empleadoService) {
        this.qrSecretsRepository = qrSecretsRepository;
        this.empleadoService = empleadoService;
    }

    public boolean validar(QRSecrets entity, String caso) throws Exception {
        if (entity.getQrKey() == null || entity.getQrKey().isBlank()) {
            throw new ErrorServiceException("QRKey no puede quedar vacía");
        }
        return false;
    }

    @Scheduled(cron = "0 0 4 ? * SUN")
    private void updateQRKey() {
        List<QRSecrets> qrSecrets = qrSecretsRepository.findByEliminadoFalse();
        QRSecrets qrSecrets1;
        if (qrSecrets.isEmpty()) {
            qrSecrets1 = new QRSecrets();
        }
        else {
            qrSecrets1 = qrSecrets.getFirst();
        }
        qrSecrets1.setQrKey(UUID.randomUUID().toString());
        qrSecretsRepository.save(qrSecrets1);
    }

    public QRSecrets getKey(String empleadoId) throws ErrorServiceException {
        if (empleadoService.existsEmpleadoById(empleadoId)) {
            List<QRSecrets> list = qrSecretsRepository.findByEliminadoFalse();
            if (list.isEmpty()) {
                throw new ErrorServiceException("No se encontró una llave disponible.");
            }
            return list.getFirst();
        }
        else {
            throw new ErrorServiceException("El empleado no se encuentra validado en el sistema.");
        }
    }

    @PostConstruct
    public void initializeKey() {
        List<QRSecrets> list = qrSecretsRepository.findByEliminadoFalse();
        if (list.isEmpty()) {
            QRSecrets qrSecrets = new QRSecrets();
            qrSecrets.setQrKey(UUID.randomUUID().toString());
            qrSecretsRepository.save(qrSecrets);
        }
    }
}
