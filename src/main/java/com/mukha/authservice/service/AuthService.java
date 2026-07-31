package com.mukha.authservice.service;

import com.mukha.authservice.client.KeycloakClient;
import com.mukha.authservice.dto.request.LoginRequest;
import com.mukha.authservice.dto.request.RegisterRequest;
import com.mukha.authservice.dto.request.UserCreateDto;
import com.mukha.authservice.dto.response.TokenResponse;
import com.mukha.authservice.dto.response.UserResponseDto;
import com.mukha.authservice.exception.InvalidCredentialsException;
import com.mukha.authservice.exception.InvalidTokenException;
import com.mukha.authservice.exception.RegistrationException;
import com.mukha.authservice.exception.UserServiceException;
import com.mukha.authservice.mapper.UserMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final KeycloakUserService keycloakUserService;
    private final UserServiceGateway userServiceGateway;
    private final KeycloakClient keycloakClient;
    private final UserMapper userMapper;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public UserResponseDto register(RegisterRequest request) {
        String kcUserId = keycloakUserService.createUser(request);
        try {
            UserCreateDto dto = userMapper.toUserServiceDto(request, kcUserId);
            UserResponseDto dbUser = userServiceGateway.createUser(dto);
            return userMapper.toResponse(request, dbUser.id(), kcUserId);
        } catch (Exception e) {
            log.warn("Registration failed after Keycloak user {} was created, rolling back", kcUserId, e);
            keycloakUserService.deleteUser(kcUserId);
            if (e instanceof UserServiceException use) throw use;
            throw new RegistrationException();
        }
    }

    public TokenResponse login(LoginRequest request) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("grant_type", "client_credentials");

            return keycloakClient.getToken(formData);
        } catch (FeignException.Unauthorized | FeignException.BadRequest e) {
            log.warn("Authentication failed via Feign: status={}, body={}", e.status(), e.contentUTF8());
            throw new InvalidCredentialsException();
        }
    }

    public TokenResponse refreshToken(String refreshToken) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("grant_type", "refresh_token");
            formData.add("refresh_token", refreshToken);

            return keycloakClient.getToken(formData);
        } catch (feign.FeignException.Unauthorized | feign.FeignException.BadRequest e) {
            log.warn("Refresh token rotation failed via Feign: status={}, body={}", e.status(), e.contentUTF8());
            throw new InvalidTokenException();
        }
    }
}