package com.jdbayer.prueba.api.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    //TODO: Agregar validacion de pattern
    @NotBlank(message = "The email cannot be empty")
    @Email(message = "This email is not valid")
    private String email;

    //TODO: Agregar validacion de pattern
    @NotBlank(message = "The password cannot be empty")
    private String password;
}
