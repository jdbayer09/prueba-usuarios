package com.jdbayer.prueba.api.services.impl;

import com.jdbayer.prueba.api.exceptions.NotExistUserException;
import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.api.models.mappers.UserMapper;
import com.jdbayer.prueba.api.repositories.UserRepository;
import com.jdbayer.prueba.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String NOT_EXIST_USER_MESSAGE = "User not found";


    @Override
    @Transactional
    public UserDTO createUser(UserDTO user) {
        return null;
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO user) {
        return null;
    }

    @Override
    @Transactional
    public void validateSessionUser(UserDTO user) {

    }

    @Override
    @Transactional
    public void disableUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotExistUserException(NOT_EXIST_USER_MESSAGE));
        user.setActive(true);
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
