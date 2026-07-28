package com.mukha.authservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreateDto(UUID keycloakUUID,
                            String name,
                            String surname,
                            LocalDate birthDate,
                            String email,
                            Boolean active) {}
