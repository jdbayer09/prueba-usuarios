package com.jdbayer.prueba.api.services;

import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.requests.PhoneRequest;

import java.util.List;
import java.util.UUID;

public interface PhoneService {
    PhoneDTO create(PhoneRequest phone, UserDTO user);
    PhoneDTO update(UUID phoneId, PhoneRequest phone, UserDTO user);
    List<PhoneDTO> saveAll(List<PhoneRequest> phones, UserDTO user);
}
