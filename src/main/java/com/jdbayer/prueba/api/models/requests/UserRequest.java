package com.jdbayer.prueba.api.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "The email field cannot be empty")
    //@Email(message = "You must enter a valid email address")
    @Schema(description = "User's email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "This email is not valid")
    private String email;

    @NotBlank(message = "The password cannot be empty")
    @Schema(description = "User's password")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$", message = "The password must be at least 8 characters long, including an uppercase letter, a lowercase letter, a number and a special character.")
    private String password;

    //@NotNull(message = "A minimum of one telephone is required")
    @Schema(description = "List phones")
    @Valid
    @NotEmpty(message = "A minimum of one telephone is required")
    private List<PhoneRequest> phones;
}
