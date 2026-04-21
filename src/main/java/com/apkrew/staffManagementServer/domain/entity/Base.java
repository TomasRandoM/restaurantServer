package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Base {
    @Id
    @GeneratedValue
    @UuidGenerator
    protected String id;

    protected boolean eliminado;
}
