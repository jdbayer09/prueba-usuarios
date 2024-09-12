package com.jdbayer.prueba.api.services;

import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.api.models.requests.UserRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO createUser(UserRequest user);
    UserDTO updateUser(UserDTO user);
    void validateSessionUser(UUID userId, String sessionToken);
    UserDetailDTO findUserByEmail(String email);
    void disableUser(UUID id);
    void enableUser(UUID id);
    UserDTO findUserById(UUID id);
    List<UserDTO> findAllUsers();

}
