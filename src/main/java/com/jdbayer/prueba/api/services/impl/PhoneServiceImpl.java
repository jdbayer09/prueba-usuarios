package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.exceptions.NotExistPhoneException;
import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.entities.PhoneEntity;
import com.jdbayer.prueba.api.models.mappers.PhoneMapper;
import com.jdbayer.prueba.api.repositories.PhoneRepository;
import com.jdbayer.prueba.api.services.PhoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    @Transactional
    public PhoneDTO create(PhoneDTO phone) {
        var phoneEntity = configurePhoneEntity(phone, new PhoneEntity());
        return phoneMapper.entityToDto(phoneEntity);
    }

    @Override
    @Transactional
    public PhoneDTO update(PhoneDTO phone) {
        var phoneEntity = configurePhoneEntity(
                    phone,
                    phoneRepository.findById(
                            phone.getId()).orElseThrow(() -> new NotExistPhoneException("Este telefono no existe")));
        return phoneMapper.entityToDto(phoneEntity);
    }

    private PhoneEntity configurePhoneEntity(PhoneDTO phoneDTO, PhoneEntity phoneEntity) {
        phoneEntity.setNumber(phoneDTO.getNumber());
        phoneEntity.setCityCode(phoneDTO.getCityCode());
        phoneEntity.setCountryCode(phoneDTO.getCountryCode());
        return phoneRepository.save(phoneEntity);
    }
}
