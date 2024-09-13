package com.jdbayer.prueba.api.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@Schema(description = "Information required to create or update a phone")
public class PhoneRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 3229430761573639657L;

    @Schema(description = "Phone City Code")
    private UUID id;

    @NotNull(message = "The number field cannot be empty")
    @Min(value = 1, message = "Min value is 1")
    @Schema(description = "Phone number")
    private long number;

    @NotNull(message = "The city code field cannot be empty")
    @Min(value = 1, message = "Min value is 1")
    @Schema(description = "Phone City Code")
    private int cityCode;

    @NotNull(message = "The country code field cannot be empty")
    @Min(value = 1, message = "Min value is 1")
    @Schema(description = "Phone Country Code")
    private int countryCode;
}
