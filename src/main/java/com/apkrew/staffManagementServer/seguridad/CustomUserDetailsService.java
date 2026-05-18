package com.apkrew.staffManagementServer.seguridad;

import com.apkrew.staffManagementServer.domain.service.UsuarioServiceImpl;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userService.searchByEmail(username);
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
            permisos.add(p);
            return UserPrincipal.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .authorities(permisos)
                    .password(user.getPassword())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
