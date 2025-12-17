package com.gustavo.vaultapp.config;

import com.gustavo.vaultapp.model.UserRole;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Component
public class VaultAuthProvider implements AuthenticationProvider {
    private final VaultClient vaultClient;

    public VaultAuthProvider(VaultClient vaultClient) {
        this.vaultClient = vaultClient;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        try {
            var res = vaultClient.loginUserpass(username, password);
            UserRole role = mapPoliciesToRole(res.policies());
            if (role == null) throw new BadCredentialsException("Rol no autorizado");
            List<SimpleGrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            return new UsernamePasswordAuthenticationToken(username, null, auths);
        } catch (Exception e) {
            throw new BadCredentialsException("Login Vault fallido: " + e.getMessage());
        }
    }

    private UserRole mapPoliciesToRole(String[] policies) {
        var set = Arrays.stream(policies).map(String::toLowerCase).toList();
        if (set.contains("admin")) return UserRole.ADMINISTRADOR;
        if (set.contains("aprobador")) return UserRole.APROBADOR;
        if (set.contains("registrador")) return UserRole.REGISTRADOR;
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}