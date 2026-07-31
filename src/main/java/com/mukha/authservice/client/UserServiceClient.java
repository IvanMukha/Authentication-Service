package com.mukha.authservice.client;

import com.mukha.authservice.dto.request.UserCreateDto;
import com.mukha.authservice.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${services.user-service.name}", url = "${services.user-service.url}")
public interface UserServiceClient {

    @PostMapping("/api/users")
    UserResponseDto createUserInDb(@RequestBody UserCreateDto dto);
}
