package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.exceptions.NotExistUserException;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.api.models.entities.UserEntity;
import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.models.requests.UserRequest;
import com.jdbayer.prueba.api.repositories.UserRepository;
import com.jdbayer.prueba.api.services.PhoneService;
import com.jdbayer.prueba.config.security.jwt.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private UserEntity userEntity;
    private UserDTO userDTO;
    private UUID userId;

    @BeforeEach
    void config() {
        userId = UUID.randomUUID();
        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password123");
        userRequest.setPhones(new ArrayList<>());

        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setPhones(new ArrayList<>());

        userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("encodedPassword");
        userDTO.setToken("token");
        userDTO.setPhones(new ArrayList<>());
    }

    @Test
    void createUser() throws IOException {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.createToken((UserDetailDTO) any())).thenReturn("token");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.entityToDto(any(UserEntity.class))).thenReturn(userDTO);
        when(phoneService.saveAll(anyList(), any(UserDTO.class))).thenReturn(List.of());

        UserDTO result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("token", result.getToken());
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.entityToDto(any(UserEntity.class))).thenReturn(userDTO);
        when(phoneService.saveAll(anyList(), any(UserDTO.class))).thenReturn(List.of());

        UserDTO result = userService.updateUser(userRequest, userId);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void validateSessionUser() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        userService.validateSessionUser(userId, "newSessionToken");
        assertEquals("newSessionToken", userEntity.getToken());
    }

    @Test
    void disableUser_success() {
        userEntity.setActive(true);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        userService.disableUser(userEntity.getId());
        assertFalse(userEntity.isActive());
        assertNotNull(userEntity.getModified());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void disableUser_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotExistUserException.class, () -> {
            userService.disableUser(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void enableUser_success() {
        userEntity.setActive(false);
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        userService.enableUser(userEntity.getId());
        assertTrue(userEntity.isActive());
        assertNotNull(userEntity.getModified());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void enableUse_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotExistUserException.class, () -> {
            userService.enableUser(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findUserByEmail_success() {
        String email = "john.doe@example.com";
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDetailDTO(userEntity)).thenReturn(userDetailDTO);
        UserDetailDTO result = userService.findUserByEmail(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPass());
    }

    @Test
    void findUserByEmail_userNotFound() {
        String email = "john.doe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotExistUserException.class, () -> {
            userService.findUserByEmail(email);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findUserById_success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDto(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void findUserById_userNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotExistUserException.class, () -> {
            userService.findUserById(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findAllUsers_success() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(UUID.randomUUID());
        userEntity1.setName("John Doe");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(UUID.randomUUID());
        userEntity2.setName("Jane Doe");

        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(userEntity1.getId());
        userDTO1.setName("John Doe");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(userEntity2.getId());
        userDTO2.setName("Jane Doe");

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.entityToDto(userEntity1)).thenReturn(userDTO1);
        when(userMapper.entityToDto(userEntity2)).thenReturn(userDTO2);

        List<UserDTO> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test
    void findAllUsers_emptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDTO> result = userService.findAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}