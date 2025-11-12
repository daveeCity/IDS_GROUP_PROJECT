package it.unicam.cs.filieraagricola.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // <-- 1. AGGIUNGI QUESTO IMPORT

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // --- 2. USA LA NUOVA SINTASSI ---
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll() // Sostituisce .anyRequest()
                )
                // 3. Sintassi aggiornata anche per frameOptions (per H2-Console)
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));

        return http.build();
    }

    // NOTA: Per un'applicazione reale, dovrai aggiungere qui i Bean per
    // PasswordEncoder, AuthenticationManager, ecc.
}