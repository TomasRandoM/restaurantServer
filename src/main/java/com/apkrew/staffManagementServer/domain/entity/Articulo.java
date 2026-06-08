package com.apkrew.staffManagementServer.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Audited
public class Articulo extends Base{
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private boolean sinTAC;
    private boolean esIngrediente;

    @ManyToOne
    @JoinColumn(name = "unidad_de_medida_id")
    private UnidadDeMedida unidadDeMedida;

    @ManyToOne
    @JoinColumn(name = "imagen_id")
    @JsonIgnoreProperties({"contenido"})
    private Imagen imagen;

    @OneToMany(mappedBy = "articulo")
    @JsonIgnoreProperties("articulo")
    @ToString.Exclude
    private List<Stock> stocks;
}
