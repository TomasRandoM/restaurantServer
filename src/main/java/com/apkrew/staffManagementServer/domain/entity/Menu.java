package com.apkrew.staffManagementServer.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Menu extends Base {

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private double precio;

    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleMenu> detalles;

    @ManyToOne
    @JoinColumn(name = "imagen_id")
    @JsonIgnoreProperties({"contenido"})
    private Imagen imagen;
}
