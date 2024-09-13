package com.jdbayer.prueba.api.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
public class PhoneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2863456708462897942L;

    private UUID id;
    private long number;
    private int cityCode;
    private int countryCode;
}
