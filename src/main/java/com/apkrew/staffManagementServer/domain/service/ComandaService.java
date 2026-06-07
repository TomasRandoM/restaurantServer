package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ComandaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.ComandaResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleComandaRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Comanda;

import java.util.List;

public interface ComandaService extends BaseService<Comanda, String> {
    ComandaResponseDTO saveFromDTO(ComandaRequestDTO dto) throws Exception;
    ComandaResponseDTO updateFromDTO(String id, ComandaRequestDTO dto) throws Exception;
    ComandaResponseDTO findResponseById(String id) throws Exception;
    List<ComandaResponseDTO> findAllResponse() throws Exception;
    org.springframework.data.domain.Page<ComandaResponseDTO> findAllResponse(org.springframework.data.domain.Pageable pageable) throws Exception;
    
    ComandaResponseDTO agregarDetalleFromDTO(String comandaId, DetalleComandaRequestDTO detalleDto) throws Exception;
    ComandaResponseDTO facturarComanda(String comandaId, String formaPagoId, String promocionId) throws Exception;
    ComandaResponseDTO entregarComanda(String comandaId) throws Exception;
    ComandaResponseDTO marcarEntregaFallida(String comandaId) throws Exception;
    ComandaResponseDTO anularComanda(String comandaId) throws Exception;
}
