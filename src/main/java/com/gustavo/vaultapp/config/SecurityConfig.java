package com.gustavo.vaultapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AuthenticationProvider vaultAuthProvider;

    public SecurityConfig(AuthenticationProvider vaultAuthProvider) {
        this.vaultAuthProvider = vaultAuthProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(vaultAuthProvider)
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/login", "/css/**").permitAll()

                        // Solo ADMINISTRADOR y REGISTRADOR pueden registrar
                        .requestMatchers("/register", "/register/**").hasAnyRole("ADMINISTRADOR", "REGISTRADOR")

                        // Solo ADMINISTRADOR y APROBADOR pueden ver la bandeja
                        .requestMatchers("/requests", "/requests/**").hasAnyRole("ADMINISTRADOR", "APROBADOR")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // ⚠️ Deshabilitado para simplificar, en producción habilitar

        return http.build();
    }
}