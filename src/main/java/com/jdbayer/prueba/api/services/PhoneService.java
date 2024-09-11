package com.jdbayer.prueba.api.services;

import com.jdbayer.prueba.api.models.dto.PhoneDTO;

public interface PhoneService {
    PhoneDTO create(PhoneDTO phone);
    PhoneDTO update(PhoneDTO phone);
}
