package com.jdbayer.prueba.api.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data returned when creating a user")
public record CreatedUserResponse(
        @Schema(description = "Unique identifier")
        UUID id,
        @Schema(description = "Names")
        String name,
        @Schema(description = "Unique email")
        String email,
        @Schema(description = "List Phones")
        List<PhoneResponse> phones,
        @Schema(description = "Created Date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMMM-yyyy hh:mm a")
        ZonedDateTime created,
        @Schema(description = "Session Token")
        String token
) {


}
