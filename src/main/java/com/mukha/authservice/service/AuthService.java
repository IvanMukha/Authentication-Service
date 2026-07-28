package com.mukha.authservice.service;

import com.mukha.authservice.dto.request.LoginRequest;
import com.mukha.authservice.dto.request.RegisterRequest;
import com.mukha.authservice.dto.UserCreateDto;
import com.mukha.authservice.dto.response.TokenResponse;
import com.mukha.authservice.dto.response.UserResponseDto;
import com.mukha.authservice.exception.RegistrationException;
import com.mukha.authservice.exception.UserServiceException;
import com.mukha.authservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final KeycloakUserService keycloakUserService;
    private final UserServiceGateway userServiceGateway;
    private final UserMapper userMapper;
    public UserResponseDto register(RegisterRequest request) {
        String kcUserId = keycloakUserService.createUser(request);
        try {
            UserCreateDto dto = userMapper.toUserServiceDto(request, kcUserId);
            UserResponseDto dbUser = userServiceGateway.createUser(dto);
            return userMapper.toResponse(request, dbUser.id(),kcUserId);
        } catch (Exception e) {
            log.warn("Registration failed after Keycloak user {} was created, rolling back", kcUserId, e);
            keycloakUserService.deleteUser(kcUserId);
            if (e instanceof UserServiceException use) throw use;
            throw new RegistrationException();
        }
    }
    public TokenResponse login(LoginRequest loginRequest){
    return keycloakUserService.login(loginRequest);
    }
}