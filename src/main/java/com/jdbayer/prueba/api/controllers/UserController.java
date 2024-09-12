package com.jdbayer.prueba.api.controllers;

import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.models.requests.UserRequest;
import com.jdbayer.prueba.api.models.responses.CreatedUserResponse;
import com.jdbayer.prueba.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Credentials are required to access this resource", content =
        @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)))
})
@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;
        private final UserMapper userMapper;

        @PostMapping("/register")
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Register User")
        public ResponseEntity<CreatedUserResponse> registerUser(@RequestBody @Valid UserRequest user) {
                var resp = userMapper.dtoToCreatedResponse(userService.createUser(user));
                return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        }
}
