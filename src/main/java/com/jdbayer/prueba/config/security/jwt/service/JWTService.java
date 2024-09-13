package com.jdbayer.prueba.config.security.jwt.service;

import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface JWTService {
    String TOKEN_PREFIX = "Bearer ";
    String createToken(Authentication auth) throws IOException;
    String createToken(UserDetailDTO userDetailDTO) throws IOException;
    boolean validateToken(String token);
    String getUserApp(String token);
    Collection<SimpleGrantedAuthority> getRoles(String token) throws IOException;
    long getExpirationTokenMillis();
}
