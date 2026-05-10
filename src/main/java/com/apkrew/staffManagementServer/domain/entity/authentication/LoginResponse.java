package com.apkrew.staffManagementServer.domain.entity.authentication;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String accessToken;
}
