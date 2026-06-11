package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.Comanda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ComandaService extends BaseService<Comanda, String> {
    Comanda facturarComanda(String comandaId, String formaPagoId, String promocionId) throws Exception;
    Comanda entregarComanda(String comandaId) throws Exception;
    Comanda marcarEntregaFallida(String comandaId) throws Exception;
    Comanda anularComanda(String comandaId) throws Exception;
    ComandaResponseDTO obtenerComandaDTO(String id) throws Exception;
    Page<ComandaResponseDTO> obtenerComandasDTO(Pageable pageable) throws Exception;
    List<ComandaResponseDTO> obtenerComandasDTO() throws Exception;
    ComandaResponseDTO convertToResponseDTO(Comanda comanda);
}
