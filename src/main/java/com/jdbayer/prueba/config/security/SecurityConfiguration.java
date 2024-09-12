package com.jdbayer.prueba.config.security;

import com.jdbayer.prueba.api.services.UserService;
import com.jdbayer.prueba.config.security.jwt.filter.JWTAuthenticationFilter;
import com.jdbayer.prueba.config.security.jwt.filter.JWTAuthorizationFilter;
import com.jdbayer.prueba.config.security.jwt.service.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JWTService jwtService;
    private final AuthenticationConfiguration authConfig;
    private final UserService userService;

    public SecurityConfiguration(JWTService jwtService, AuthenticationConfiguration authConfig, UserService userService) {
        this.jwtService = jwtService;
        this.authConfig = authConfig;
        this.userService = userService;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Content-Disposition", "timeout"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*", "https://localhost"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static final String[] freePaths = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/public/**",
            "/api/v1/users/register"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests()
                .requestMatchers(freePaths)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authConfig.getAuthenticationManager(), jwtService, userService))
                .addFilter(new JWTAuthorizationFilter(authConfig.getAuthenticationManager(), jwtService))
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();
    }
}
