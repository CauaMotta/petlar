package br.com.ocauamotta.PetLar.dtos.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

/**
 * DTO utilizado para receber as credenciais de login na API.
 *
 * @param email O e-mail do usuário.
 * @param password A senha fornecida pelo usuário.
 */
@Schema(description = "DTO de Requisição contendo e-mail e senha para realizar o login.")
public record AuthRequestDto(
        @Schema(description = "E-mail do usuário para autenticação.", example = "usuario@petlar.com.br", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Formato de email inválido.")
        String email,
        @Schema(description = "Senha do usuário.", example = "MinhaSenha123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}