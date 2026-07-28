package com.mukha.authservice.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(Long id,
                              UUID keycloakUUID,
                              String name,
                              String surname,
                              LocalDate birthDate,
                              String email,
                              Boolean active) {}
