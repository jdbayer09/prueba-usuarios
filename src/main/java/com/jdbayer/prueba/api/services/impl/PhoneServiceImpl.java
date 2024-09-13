package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.entities.PhoneEntity;
import com.jdbayer.prueba.api.models.mappers.PhoneMapper;
import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.models.requests.PhoneRequest;
import com.jdbayer.prueba.api.repositories.PhoneRepository;
import com.jdbayer.prueba.api.services.PhoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public List<PhoneDTO> saveAll(List<PhoneRequest> phones, UserDTO user) {
        var listPhones = phones.stream()
                .map(phone -> configurePhoneEntity(phone, new PhoneEntity(), user))
                .toList();
        return StreamSupport
                .stream(phoneRepository.saveAll(listPhones).spliterator(), false)
                .map(phoneMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllByUserId(UUID userId) {
        phoneRepository.deleteByUser_Id(userId);
    }

    private PhoneEntity configurePhoneEntity(PhoneRequest phoneDTO, PhoneEntity phoneEntity, UserDTO user) {
        phoneEntity.setNumber(phoneDTO.getNumber());
        phoneEntity.setCityCode(phoneDTO.getCityCode());
        phoneEntity.setCountryCode(phoneDTO.getCountryCode());
        phoneEntity.setUser(userMapper.dtoToEntity(user));
        return phoneEntity;
    }
}
