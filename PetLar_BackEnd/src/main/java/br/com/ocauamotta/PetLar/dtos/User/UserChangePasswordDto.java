package br.com.ocauamotta.PetLar.dtos.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado exclusivamente para receber a nova senha
 * bruta de um usuário no endpoint de alteração de senha.
 */
@Schema(description = "DTO de Requisição para alteração da senha do usuário autenticado.")
public record UserChangePasswordDto(
        @Schema(description = "Nova senha do usuário. Deve conter entre 6 e 50 caracteres.", example = "NovaSenhaForte456")
        @NotBlank(message = "O campo senha é obrigatório.")
        @Size(min = 6, max = 50, message = "A senha deve conter entre 6 e 50 caracteres.")
        String password
) {
}
