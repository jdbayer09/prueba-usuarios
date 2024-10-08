package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.exceptions.ErrorTokenException;
import com.jdbayer.prueba.api.exceptions.NotExistUserException;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.api.models.entities.UserEntity;
import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.models.requests.UserRequest;
import com.jdbayer.prueba.api.repositories.UserRepository;
import com.jdbayer.prueba.api.services.PhoneService;
import com.jdbayer.prueba.api.services.UserService;
import com.jdbayer.prueba.config.security.jwt.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final PhoneService phoneService;

    private static final String NOT_EXIST_USER_MESSAGE = "User not found";


    @Override
    @Transactional
    public UserDTO createUser(UserRequest user) {
        var userEntity = new UserEntity();

        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail().toLowerCase());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setActive(true);

        try {
            var token = jwtService.createToken(userMapper.entityToDetailDTO(userEntity));
            userEntity.setToken(token);
        } catch (Exception e) {
            throw new ErrorTokenException("Problemas al crear el token");
        }

        var userCreated = userMapper.entityToDto(userRepository.save(userEntity));
        userCreated.setPhones(phoneService.saveAll(user.getPhones(), userCreated));
        return userCreated;
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserRequest user, UUID id) {
        var userEntity = userRepository.findById(id).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        if (!user.getPassword().equalsIgnoreCase(userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userEntity.setEmail(user.getEmail().toLowerCase());
        userEntity.setName(user.getName());
        userEntity.setModified(ZonedDateTime.now());
        phoneService.deleteAllByUserId(id);
        var resp = userMapper.entityToDto(userRepository.save(userEntity));
        resp.setPhones(phoneService.saveAll(user.getPhones(), resp));
        return resp;
    }

    @Override
    @Transactional
    public void validateSessionUser(UUID userId, String sessionToken) {
        var user = userRepository.findById(userId).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        user.setToken(sessionToken);
        user.setLastLogin(ZonedDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disableUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        user.setActive(false);
        user.setModified(ZonedDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        user.setActive(true);
        user.setModified(ZonedDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO findUserByEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        var userDto = userMapper.entityToDetailDTO(user);
        userDto.setPass(user.getPassword());
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findUserById(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        return userMapper.entityToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        var users = StreamSupport.stream(userRepository.findAll().spliterator(), false).toList();
        if (users.isEmpty()) return List.of();
        return users.stream().map(userMapper::entityToDto).toList();
    }
}
