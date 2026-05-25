package com.apkrew.staffManagementServer.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class QRSecrets extends Base {
    private String qrKey;
}
