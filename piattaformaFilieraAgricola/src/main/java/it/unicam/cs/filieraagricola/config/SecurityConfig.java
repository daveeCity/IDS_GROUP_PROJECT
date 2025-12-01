package it.unicam.cs.filieraagricola.config;

import it.unicam.cs.filieraagricola.model.Trasformatore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // --- 1. Regole Pubbliche ---
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()


                        // --- 2. REGOLE EVENTI (Ordine Fondamentale!) ---

                        // A. L'eccezione: Solo l'ACQUIRENTE può fare POST su "partecipa"
                        // Nota: i doppi asterischi ** servono per coprire l'ID variabile
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/eventi/*/partecipa")).hasRole("ACQUIRENTE")

                        // B. Lettura Eventi: Aperta a tutti gli utenti loggati (o permitAll() se vuoi pubblico)
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/eventi/**")).authenticated()

                        // C. Regola Generale: Tutto il resto su /api/eventi (Creare, Annullare) è solo per ANIMATORE
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/eventi/**")).hasRole("ANIMATORE")

                        .requestMatchers(AntPathRequestMatcher.antMatcher( "/api/marketplace/**")).hasRole("ACQUIRENTE")

                        .requestMatchers(AntPathRequestMatcher.antMatcher( "/api/moderazione/**")).hasRole("CURATORE")

                        .requestMatchers(AntPathRequestMatcher.antMatcher( "/api/pacchi/**")).hasRole("DISTRIBUTORE")

                        .requestMatchers(AntPathRequestMatcher.antMatcher( "/api/prodotti/**")).hasAnyRole("TRASFORMATORE","PRODUTTORE","DISTRIBUTORE")

                        .requestMatchers(AntPathRequestMatcher.antMatcher( "/api/mappa/**")).permitAll()

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/social/**")).permitAll()

                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/tracciabilita/**")).hasAnyRole("PRODUTTORE", "TRASFORMATORE")

                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/tracciabilita/**")).authenticated()

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/gestore/**")).hasRole("GESTORE")

                        // --- 3. Default ---
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}