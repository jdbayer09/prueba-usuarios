package com.jdbayer.prueba.api.repositories;

import com.jdbayer.prueba.api.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
}
