package com.jdbayer.prueba.api.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contains the error scheme used when loading exceptions")
public record ErrorResponse(
        @Schema(description = "Error Message")
        String message
) {
}
