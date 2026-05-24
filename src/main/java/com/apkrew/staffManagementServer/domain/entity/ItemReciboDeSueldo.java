package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Audited
public class ItemReciboDeSueldo extends Base {

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
