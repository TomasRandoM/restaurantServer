package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.EstadoFactura;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura extends Base {

    private Long numeroFactura;

    private Date fechaFactura;

    private double totalPagado;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    @OneToMany(mappedBy = "factura")
    private List<DetalleFactura> detalles;

    @ManyToOne
    @JoinColumn(name = "forma_pago_id")
    private FormaDePago formaPago;

    @ManyToOne
    @JoinColumn(name = "promocion_id")
    private Promocion promocion;
}
