package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.TipoContacto;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@Audited
public abstract class Contacto extends Base {
    private String observacion;
    private TipoContacto tipoContacto;
}
