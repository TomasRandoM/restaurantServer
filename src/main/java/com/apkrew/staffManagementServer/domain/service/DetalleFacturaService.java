package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.DetalleFacturaRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleFacturaResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.DetalleFactura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DetalleFacturaService
        extends BaseService<DetalleFactura, String> {

    DetalleFacturaResponseDTO saveFromDTO(DetalleFacturaRequestDTO dto) throws Exception;

    DetalleFacturaResponseDTO updateFromDTO(String id, DetalleFacturaRequestDTO dto) throws Exception;

    DetalleFacturaResponseDTO convertToResponseDTO(DetalleFactura entity);

    List<DetalleFacturaResponseDTO> findAllDTO() throws Exception;

    Page<DetalleFacturaResponseDTO> findAllDTO(Pageable pageable) throws Exception;

    DetalleFacturaResponseDTO findByIdDTO(String id) throws Exception;
}
