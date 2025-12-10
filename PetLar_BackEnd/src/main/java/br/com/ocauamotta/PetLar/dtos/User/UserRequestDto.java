package br.com.ocauamotta.PetLar.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para receber dados necessários para cadastrar ou atualizar um usuário.
 * <p>
 * Este DTO é usado nas operações de criação e atualização, garantindo
 * que todos os campos obrigatórios sejam validados.
 */
public record UserRequestDto(
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "Formato de email inválido.")
        String email,
        @NotBlank(message = "O campo senha é obrigatório.")
        String password
) {
}
