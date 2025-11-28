package br.com.ocauamotta.PetLar.dtos.Auth;

import jakarta.validation.constraints.Email;

/**
 * DTO utilizado para receber as credenciais de login na API.
 *
 * @param email O e-mail do usuário.
 * @param password A senha fornecida pelo usuário.
 */
public record AuthRequestDto(
        @Email
        String email,
        String password
) {}