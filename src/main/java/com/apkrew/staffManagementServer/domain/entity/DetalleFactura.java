package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleFactura extends Base {

    private int cantidad;

    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;
}
