package br.com.ocauamotta.PetLar.dtos.Auth;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO utilizado como resposta ao realizar uma autenticação na API.
 * Este record encapsula o token gerado pelo serviço de segurança.
 *
 * @param token A {@code String} que contém o token (JWT) assinado.
 */
@Schema(description = "DTO de Resposta contendo o Token JWT gerado após a autenticação bem-sucedida.")
public record TokenResponse(
        @Schema(description = "O Token JWT assinado que deve ser usado para autenticar futuras requisições via cabeçalho 'Authorization'.",
                example = "eyJhbG...")
        String token
) {
}
