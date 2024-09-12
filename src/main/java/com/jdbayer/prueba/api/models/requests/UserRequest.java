package com.jdbayer.prueba.api.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@Schema(description = "Information required to create or update a user")
public class UserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1728078472832668151L;


    @NotBlank(message = "The name field cannot be empty")
    @Schema(description = "User names")
    private String name;

    //TODO: Validar el pattern
    @NotBlank(message = "The email field cannot be empty")
    @Email(message = "You must enter a valid email address")
    @Schema(description = "User's email")
    private String email;

    //TODO: Agregar validacion de pattern
    @NotBlank(message = "The password cannot be empty")
    @Schema(description = "User's password")
    private String password;

    @NotNull(message = "A minimum of one telephone is required")
    @Schema(description = "List phones")
    @Valid
    private List<PhoneRequest> phones;
}
