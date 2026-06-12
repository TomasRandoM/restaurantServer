package com.apkrew.staffManagementServer.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "detalleFactura", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("detalleFactura")
    private List<DetalleComanda> detallesComanda;
}
