package com.jdbayer.prueba.api.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response to phone")
public record PhoneResponse(
        @Schema(description = "Unique identifier")
        UUID id,
        @Schema(description = "Phone Number")
        long number,
        @Schema(description = "City Code Number")
        int cityCode,
        @Schema(description = "Country Code number")
        int countryCode
) {
}
