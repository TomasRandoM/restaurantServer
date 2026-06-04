package com.apkrew.staffManagementServer.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class SeccionCarta extends Base {

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Carta carta;

    @OneToMany(
            mappedBy = "seccionCarta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("seccionCarta")
    private List<DetalleSeccionCarta> detalles;

}
