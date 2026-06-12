package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.FacturaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.FacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacturaService
        extends BaseService<Factura, String> {

    FacturaResponseDTO saveFromDTO(FacturaRequestDTO dto) throws Exception;

    FacturaResponseDTO updateFromDTO(String id, FacturaRequestDTO dto) throws Exception;

    FacturaResponseDTO convertToResponseDTO(Factura entity);

    List<FacturaResponseDTO> findAllDTO() throws Exception;

    Page<FacturaResponseDTO> findAllDTO(Pageable pageable) throws Exception;

    FacturaResponseDTO findByIdDTO(String id) throws Exception;
}
