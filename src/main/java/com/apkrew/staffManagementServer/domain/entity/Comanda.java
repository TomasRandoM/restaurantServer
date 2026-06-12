package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.EstadoComanda;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Audited
public class Comanda extends Base {

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaSolicitudComanda;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaEntregaComanda;

    @Enumerated(EnumType.STRING)
    private EstadoComanda estadoComanda;

    private Long facturaNumero;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Builder.Default
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("comanda")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleComanda> detalles = new ArrayList<>();

}
