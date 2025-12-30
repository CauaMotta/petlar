package br.com.ocauamotta.PetLar.dtos.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para receber dados necessários para cadastrar um usuário.
 * <p>
 * Este DTO é usado na operação de criação, garantindo
 * que todos os campos obrigatórios sejam validados.
 */
@Schema(description = "DTO de Requisição para o cadastro de um novo usuário.")
public record UserRequestDto(
        @Schema(description = "Endereço de e-mail único do novo usuário.", example = "novo.usuario@petlar.com.br")
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "Formato de email inválido.")
        String email,
        @Schema(description = "Senha do novo usuário. Deve conter entre 6 e 50 caracteres.", example = "SenhaSegura123")
        @NotBlank(message = "O campo senha é obrigatório.")
        @Size(min = 6, max = 50, message = "A senha deve conter entre 6 e 50 caracteres.")
        String password,
        @Schema(description = "Nome do novo usuário.", example = "João da Silva")
        @NotBlank(message = "O campo nome é obrigatório.")
        @Size(min = 3, max = 110, message = "O nome deve conter entre 3 e 110 caracteres.")
        String name
) {
}
