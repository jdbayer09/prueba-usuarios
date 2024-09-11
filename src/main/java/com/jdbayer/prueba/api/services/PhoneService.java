package com.jdbayer.prueba.api.services;

import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.dto.UserDTO;

import java.util.List;

public interface PhoneService {
    PhoneDTO create(PhoneDTO phone, UserDTO user);
    PhoneDTO update(PhoneDTO phone, UserDTO user);
    List<PhoneDTO> saveAll(List<PhoneDTO> phones, UserDTO user);
}
