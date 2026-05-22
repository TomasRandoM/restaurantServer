package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.TipoImagen;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class Imagen extends Base {
    private String nombre;
    private String mime;
    @Lob
    private byte[] contenido;
    private TipoImagen tipoImagen;
}
