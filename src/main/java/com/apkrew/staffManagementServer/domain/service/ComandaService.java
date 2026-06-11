package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.entity.Comanda;

public interface ComandaService extends BaseService<Comanda, String> {
    Comanda facturarComanda(String comandaId, String formaPagoId, String promocionId) throws Exception;
    Comanda entregarComanda(String comandaId) throws Exception;
    Comanda marcarEntregaFallida(String comandaId) throws Exception;
    Comanda anularComanda(String comandaId) throws Exception;
}
