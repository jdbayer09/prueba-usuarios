package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.exceptions.NotExistPhoneException;
import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.entities.PhoneEntity;
import com.jdbayer.prueba.api.models.mappers.PhoneMapper;
import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.repositories.PhoneRepository;
import com.jdbayer.prueba.api.services.PhoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;
    private final UserMapper userMapper;

    private static final String ERROR_NOT_FOUND_PHONE = "Phone not found";

    @Override
    @Transactional
    public PhoneDTO create(PhoneDTO phone, UserDTO user) {
        var phoneEntity = configurePhoneEntity(phone, new PhoneEntity(), user);
        return phoneMapper.entityToDto(phoneRepository.save(phoneEntity));
    }

    @Override
    @Transactional
    public PhoneDTO update(PhoneDTO phone, UserDTO user) {
        var phoneEntity = configurePhoneEntity(
                    phone,
                    phoneRepository.findById(
                            phone.getId()).orElseThrow(() -> new NotExistPhoneException(ERROR_NOT_FOUND_PHONE)), user);
        return phoneMapper.entityToDto(phoneRepository.save(phoneEntity));
    }

    @Override
    @Transactional
    public List<PhoneDTO> saveAll(List<PhoneDTO> phones, UserDTO user) {
        phoneRepository.deleteByUser_Id(user.getId());
        var listPhones = phones.stream()
                .map(phone -> configurePhoneEntity(phone, new PhoneEntity(), user))
                .toList();
        return StreamSupport
                .stream(phoneRepository.saveAll(listPhones).spliterator(), false)
                .map(phoneMapper::entityToDto)
                .toList();
    }

    private PhoneEntity configurePhoneEntity(PhoneDTO phoneDTO, PhoneEntity phoneEntity, UserDTO user) {
        phoneEntity.setNumber(phoneDTO.getNumber());
        phoneEntity.setCityCode(phoneDTO.getCityCode());
        phoneEntity.setCountryCode(phoneDTO.getCountryCode());
        phoneEntity.setUser(userMapper.dtoToEntity(user));
        return phoneEntity;
    }
}
