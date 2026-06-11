package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReciboDeSueldoService extends BaseService<ReciboDeSueldo, String> {

    double calcularTotal(String id) throws Exception;

    ReciboDeSueldoResponseDTO saveFromDTO(ReciboDeSueldoRequestDTO dto) throws Exception;

    ReciboDeSueldoResponseDTO updateFromDTO(String id, ReciboDeSueldoRequestDTO dto) throws Exception;

    ReciboDeSueldoResponseDTO findResponseById(String id) throws Exception;

    List<ReciboDeSueldoResponseDTO> findAllResponse() throws Exception;

    Page<ReciboDeSueldoResponseDTO> findAllResponse(Pageable pageable) throws Exception;

    List<ReciboDeSueldoResponseDTO> findAllByEmpleadoId(String empleadoId) throws Exception;

    Page<ReciboDeSueldoResponseDTO> findAllByEmpleadoId(String empleadoId, Pageable pageable) throws Exception;

    Page<ReciboDeSueldoResponseDTO> findAllForCurrentUser(Pageable pageable) throws Exception;
}
