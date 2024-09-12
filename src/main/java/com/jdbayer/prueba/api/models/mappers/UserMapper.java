package com.jdbayer.prueba.api.models.mappers;

import com.jdbayer.prueba.api.models.dto.UserDTO;
import com.jdbayer.prueba.api.models.dto.UserDetailDTO;
import com.jdbayer.prueba.api.models.entities.UserEntity;
import com.jdbayer.prueba.api.models.responses.CreatedUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO entityToDto(UserEntity userEntity);
    UserEntity dtoToEntity(UserDTO userDTO);
    UserDetailDTO entityToDetailDTO(UserEntity userEntity);
    CreatedUserResponse dtoToCreatedResponse(UserDTO userDTO);
}
