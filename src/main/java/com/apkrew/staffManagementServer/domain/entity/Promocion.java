package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promocion extends Base {

    private double porcentajeDescuento;

    private String descripcion;

}
