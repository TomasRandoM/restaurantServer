package com.apkrew.staffManagementServer.domain.entity;

import com.apkrew.staffManagementServer.domain.enums.TipoMovimientoStock;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class MovimientoStock extends Base {
    private LocalDateTime fecha = LocalDateTime.now();
    private double cantidad;
    private TipoMovimientoStock tipoMovimiento;
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;
}
