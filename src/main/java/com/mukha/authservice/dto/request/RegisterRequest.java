package com.mukha.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message = "Login cannot be empty")
        String login,
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must contain 8 characters or more")
        String password,
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Surname cannot be empty")
        String surname,
        @NotNull(message = "Birthdate required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        Boolean active) {
}
