package com.jdbayer.prueba.api.repositories;

import com.jdbayer.prueba.api.models.entities.PhoneEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PhoneRepository extends CrudRepository<PhoneEntity, UUID> {

    void deleteByUser_Id(UUID userId);
}
