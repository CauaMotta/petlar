package br.com.ocauamotta.PetLar.dtos.Auth;

import jakarta.validation.constraints.Email;

public record AuthRequestDto(
        @Email
        String email,
        String password
) {}