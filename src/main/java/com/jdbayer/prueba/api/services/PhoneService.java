package com.jdbayer.prueba.api.services;

import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.requests.PhoneRequest;

import java.util.List;

public interface PhoneService {
    List<PhoneDTO> saveAll(List<PhoneRequest> phones, UserDTO user);
}
