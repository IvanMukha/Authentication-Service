package com.mukha.authservice.service;

import com.mukha.authservice.client.UserServiceClient;
import com.mukha.authservice.dto.UserCreateDto;
import com.mukha.authservice.dto.response.UserResponseDto;
import com.mukha.authservice.exception.UserServiceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceGateway {
    private final UserServiceClient client;
    public UserResponseDto createUser(UserCreateDto dto) {
        try {
            UserResponseDto response = client.createUserInDb(dto);
            if (response == null || response.id() == null) {
                throw new UserServiceException();
            }
            return response;
        } catch (FeignException e) {
            throw new UserServiceException();
        }
    }
}