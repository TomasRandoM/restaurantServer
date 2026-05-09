package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@Audited
public class Persona extends Base{
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String dni;
    private Date fechaNacimiento;
    @ManyToOne
    private Usuario usuario;
}
