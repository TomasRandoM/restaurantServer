package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class Carta extends Base {

    private String nombre;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;

    private boolean activo;

    @OneToMany(
            mappedBy = "carta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SeccionCarta> secciones;
}
