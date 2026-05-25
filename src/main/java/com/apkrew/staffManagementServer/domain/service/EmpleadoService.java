package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.EmpleadoRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Empleado;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import org.springframework.web.multipart.MultipartFile;

public interface EmpleadoService extends BaseService<Empleado, String> {
    Empleado crearEmpleado(EmpleadoRequestDTO dto, MultipartFile foto) throws ErrorServiceException;
    public EmpleadoRequestDTO buscarEmpleado(String empleadoId) throws ErrorServiceException;
    public boolean existsEmpleadoByDni(String dni);
}
