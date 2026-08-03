package com.mukha.authservice.mapper;

import com.mukha.authservice.dto.request.SignUpRequest;
import com.mukha.authservice.dto.request.UserCreateDto;
import com.mukha.authservice.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "keycloakUUID", source = "kcUserId")
    UserCreateDto toUserServiceDto(SignUpRequest request, String kcUserId);

    @Mapping(target = "id", source = "dbUserId")
    @Mapping(target = "keycloakUUID", source = "kcUserId")
    UserResponseDto toResponse(SignUpRequest request, Long dbUserId, String kcUserId);
}


