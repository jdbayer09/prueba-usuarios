package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.models.dto.PhoneDTO;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.entities.PhoneEntity;
import com.jdbayer.prueba.api.models.entities.UserEntity;
import com.jdbayer.prueba.api.models.mappers.PhoneMapper;
import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.models.requests.PhoneRequest;
import com.jdbayer.prueba.api.repositories.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PhoneServiceImplTest {

    @InjectMocks
    private PhoneServiceImpl phoneService;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private PhoneMapper phoneMapper;

    @Mock
    private UserMapper userMapper;

    @Test
    void testSaveAll() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());

        PhoneRequest phoneRequest = new PhoneRequest();
        phoneRequest.setNumber(123456789);
        phoneRequest.setCityCode(1);
        phoneRequest.setCountryCode(57);

        List<PhoneRequest> phoneRequests = List.of(phoneRequest);

        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setNumber(123456789);
        phoneEntity.setCityCode(1);
        phoneEntity.setCountryCode(57);

        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber(123456789);
        phoneDTO.setCityCode(1);
        phoneDTO.setCountryCode(57);

        when(userMapper.dtoToEntity(userDTO)).thenReturn(new UserEntity());
        when(phoneRepository.saveAll(anyList())).thenReturn(List.of(phoneEntity));
        when(phoneMapper.entityToDto(phoneEntity)).thenReturn(phoneDTO);

        List<PhoneDTO> result = phoneService.saveAll(phoneRequests, userDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(123456789, result.get(0).getNumber());

        verify(phoneRepository).saveAll(anyList());
        verify(phoneMapper).entityToDto(phoneEntity);
    }

    @Test
    void testDeleteAllByUserId(){
        UUID userId = UUID.randomUUID();
        phoneService.deleteAllByUserId(userId);
        verify(phoneRepository, times(1)).deleteByUser_Id(userId);
    }
}