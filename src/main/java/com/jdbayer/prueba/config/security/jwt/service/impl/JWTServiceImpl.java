package com.jdbayer.prueba.config.security.jwt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdbayer.prueba.api.exceptions.ErrorTokenException;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.config.security.SimpleGrantedAuthorityMixin;
import com.jdbayer.prueba.config.security.jwt.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

    @Value("${nisum.jwt.secret-key}")
    private String secretKey;
    @Value("${nisum.jwt.application-name}")
    private String applicationName;
    @Value("${nisum.jwt.expiration-token-hours}")
    private long expirationTokenHours;

    @Override
    public String getUserApp(String token) {
        return getClaimsToken(token, Claims::getSubject);
    }

    @Override
    public String createToken(Authentication auth) throws IOException {
        return createTokenAction(auth);
    }

    @Override
    public String createToken(UserDetailDTO userDetailDTO) throws IOException {
        return createTokenAction(userDetailDTO);
    }

    private String createTokenAction(Object object) throws IOException{
        String claims = "";
        String email = "";
        if (object instanceof Authentication auth) {
            email = auth.getName();
            claims = new ObjectMapper().writeValueAsString(auth.getAuthorities());
        } else if (object instanceof UserDetailDTO auth) {
            claims = new ObjectMapper().writeValueAsString(auth.getAuthorities());
            email = auth.getEmail();
        } else {
            throw new ErrorTokenException("No se puede generar el token");
        }

        Claims extraClaims = Jwts.claims();
        extraClaims.put("authorities", claims);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(getExpirationTokenMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .setId(applicationName)
                .compact();
    }
    @Override
    public long getExpirationTokenMillis() {
        Instant expirationToken = Instant.now().plus(expirationTokenHours, ChronoUnit.HOURS);
        return expirationToken.toEpochMilli();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return getClaimsToken(token, Claims::getExpiration);
    }
    @Override
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
    @Override
    public Collection<SimpleGrantedAuthority> getRoles(String token) throws IOException {
        Object roles = getClaimsToken(token, claims -> claims.get("authorities"));
        return Arrays.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
    }
    private <T> T getClaimsToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(resolve(token))
                .getBody();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private String resolve(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }
}
