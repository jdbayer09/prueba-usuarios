package com.jdbayer.prueba.api.repositories;

import com.jdbayer.prueba.api.models.entities.PhoneEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PhoneRepository extends CrudRepository<PhoneEntity, UUID> {
    @Modifying
    @Query("DELETE PhoneEntity p WHERE p.user.id = ?1")
    void deleteByUser_Id(UUID userId);
}
