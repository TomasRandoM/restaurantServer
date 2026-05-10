package com.apkrew.staffManagementServer.controller;

import com.apkrew.staffManagementServer.domain.entity.Usuario;
import com.apkrew.staffManagementServer.domain.entity.authentication.LoginResponse;
import com.apkrew.staffManagementServer.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated Usuario request) {
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }
}
