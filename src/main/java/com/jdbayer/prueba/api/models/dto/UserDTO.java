package com.jdbayer.prueba.api.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7349383502416203288L;

    private UUID id;
    private String name;
    private String email;
    private String password;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private ZonedDateTime lastLogin;
    private String token;
    private boolean active;
    private List<PhoneDTO> phones;
}
