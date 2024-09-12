package com.jdbayer.prueba.config.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.api.models.requests.AuthenticationRequest;
import com.jdbayer.prueba.api.models.responses.AuthenticationResponse;
import com.jdbayer.prueba.api.models.responses.ErrorResponse;
import com.jdbayer.prueba.config.security.jwt.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        this.jwtService = jwtService;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationRequest authenticationRequest;
        Authentication authResp;
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);
        } catch (IOException e) {
            throw new UsernameNotFoundException("Problems reading the data, check that all are correct: 'E-mail', 'Password'");
        }

        try {
            authResp = authManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authenticationRequest.getEmail(),
                                    authenticationRequest.getPassword()
                            )
                    );
        } catch (AuthenticationException ae) {
            if (ae.getMessage().equals("User is disabled"))
                throw new UsernameNotFoundException("User is disabled!", ae);
            else if (ae.getMessage().equals("Bad credentials"))
                throw new UsernameNotFoundException("Bad credentials!", ae);
            else
                throw new UsernameNotFoundException("Error entering your credentials!", ae);
        }
        return authResp;
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserDetailDTO userData = ((UserDetailDTO) authResult.getPrincipal());

        String token = jwtService.createToken(authResult);
        response.getWriter().write(new ObjectMapper().writeValueAsString(getAuthenticationResponse(token, userData)));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
    }

    private AuthenticationResponse getAuthenticationResponse(String token, UserDetailDTO userData) {
        AuthenticationResponse body = new AuthenticationResponse();

        body.setToken(token);
        body.setEmail(userData.getEmail());
        body.setId(userData.getId());
        body.setName(userData.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date expirationToken = new Date(jwtService.getExpirationTokenMillis());
        body.setExpirationToken(sdf.format(expirationToken));
        return body;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        new ErrorResponse(
                                "¡Invalid credentials! \n " + failed.getMessage()
                        )
                )
        );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
    }
}
