package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;

import java.util.List;

public interface ReciboDeSueldoService extends BaseService<ReciboDeSueldo, String> {

    double calcularTotal(String id) throws Exception;

    ReciboDeSueldoResponseDTO saveFromDTO(ReciboDeSueldoRequestDTO dto) throws Exception;

    ReciboDeSueldoResponseDTO updateFromDTO(String id, ReciboDeSueldoRequestDTO dto) throws Exception;

    ReciboDeSueldoResponseDTO findResponseById(String id) throws Exception;

    List<ReciboDeSueldoResponseDTO> findAllResponse() throws Exception;

    org.springframework.data.domain.Page<ReciboDeSueldoResponseDTO> findAllResponse(org.springframework.data.domain.Pageable pageable) throws Exception;
}
