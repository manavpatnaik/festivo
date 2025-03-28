package com.manav.festivo.config;

import com.manav.festivo.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {
    private final JwtFilter jwtFilter;
    private final AppProperties appProperties;

    @Bean
    public String getUploadDir() {
        return appProperties.getUploadDir();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated() // Allow authenticated users to view (ownership enforced at controller)
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated() // Allow authenticated users to update (ownership enforced at controller)
                        .requestMatchers("/api/users/**").hasRole("ADMIN") // All other user operations restricted to ADMIN
                        // Service provider endpoints
                        .requestMatchers(HttpMethod.GET, "/api/service-providers/**").hasAnyRole("CUSTOMER", "SERVICE_PROVIDER", "ADMIN") // Allow viewing for all roles
                        .requestMatchers(HttpMethod.POST, "/api/service-providers/**").hasAnyRole("SERVICE_PROVIDER", "ADMIN") // Allow creation for SERVICE_PROVIDER and ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/service-providers/**").hasAnyRole("SERVICE_PROVIDER", "ADMIN") // Allow updates for SERVICE_PROVIDER and ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/service-providers/**").hasAnyRole("SERVICE_PROVIDER", "ADMIN") // Allow deletion for SERVICE_PROVIDER and ADMIN
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }
}
