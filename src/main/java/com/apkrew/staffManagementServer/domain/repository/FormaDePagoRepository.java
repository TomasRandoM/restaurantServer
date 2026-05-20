package com.apkrew.staffManagementServer.domain.repository;

import com.apkrew.staffManagementServer.domain.entity.FormaDePago;
import com.apkrew.staffManagementServer.domain.enums.MetodoPago;

public interface FormaDePagoRepository extends BaseRepository<FormaDePago, String>{
    boolean existsByMetodoPagoAndEliminadoFalse(MetodoPago metodoPago);

    FormaDePago findByMetodoPagoAndEliminadoFalse(MetodoPago metodoPago);
}
