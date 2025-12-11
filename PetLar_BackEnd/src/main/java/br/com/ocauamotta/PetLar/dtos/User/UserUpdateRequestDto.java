package br.com.ocauamotta.PetLar.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para receber dados para a atualização
 * das informações de um usuário existente.
 * <p>
 * Contém regras de validação para garantir a integridade dos novos dados.
 */
public record UserUpdateRequestDto(
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "Formato de email inválido.")
        String email,
        @NotBlank(message = "O campo nome é obrigatório.")
        @Size(min = 3, max = 110, message = "O nome deve conter entre 3 e 110 caracteres.")
        String name
) {
}
