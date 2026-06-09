package com.apkrew.staffManagementServer.domain.dto;

import com.apkrew.staffManagementServer.domain.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class ClienteRequestDTO {

    // Persona
    private String nombre;
    private String apellido;
    private TipoDocumentacion tipoDocumentacion;
    private String dni;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    // Direccion
    private String direccionId;
    private String direccionEstadia;

    // Contacto
    private TipoContacto tipoContacto;
    private String contactoTelefono;
    private TipoTelefono tipoTelefono;
    private String observacion;
}
