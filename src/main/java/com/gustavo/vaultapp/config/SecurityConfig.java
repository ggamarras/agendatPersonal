package com.gustavo.vaultapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
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
                        .requestMatchers("/login", "/css/**").permitAll()
                        //.requestMatchers("/register/**").hasAnyRole("ADMINISTRADOR","REGISTRADOR")
                        .requestMatchers("/requests/**").hasAnyRole("ADMINISTRADOR","APROBADOR","REGISTRADOR")
                        .requestMatchers("/register", "/register/**").hasAnyRole("ADMINISTRADOR", "REGISTRADOR")
                        .requestMatchers("/requests/**").hasAnyRole("ADMINISTRADOR","APROBADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
                .csrf(csrf -> csrf.disable()); // para simplificar; en prod habilitar y usar tokens
        return http.build();
    }
}