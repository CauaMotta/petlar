package br.com.ocauamotta.PetLar.dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado exclusivamente para receber a nova senha
 * bruta de um usuário no endpoint de alteração de senha.
 */
public record UserChangePasswordDto(
        @NotBlank(message = "O campo senha é obrigatório.")
        @Size(min = 6, max = 50, message = "A senha deve conter entre 6 e 50 caracteres.")
        String password
) {
}
