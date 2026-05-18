package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.RolUsuario;
import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.apkrew.staffManagementServer.domain.enums.TipoEmpleado;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class EmpleadoRequestDTO {

    // Persona
    private String nombre;
    private String apellido;
    private TipoDocumentacion tipoDocumentacion;
    private String dni;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    // Empleado
    private TipoEmpleado tipoEmpleado;

    // Usuario
    private String email;
    private String password;
    private RolUsuario rol;

    // Direccion
    private String calle;
    private String numeracion;
    private String barrio;
    private String manzanaPiso;
    private String casaDepartamento;
    private String referencia;
    private String localidadId;
}
