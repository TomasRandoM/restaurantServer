package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.TipoDocumentacion;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public abstract class Persona extends Base{
    private String nombre;
    private String apellido;
    private TipoDocumentacion tipoDocumentacion;
    private String dni;
    private Date fechaNacimiento;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Imagen imagen;
    @ManyToOne
    private Direccion direccion;
    @ManyToOne
    private Contacto contacto;

}
