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
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJQZXRMYXIiLCJzdWIiOiIxMjM0NTY3ODkwIiwiZXhwIjoxNTE2MjM5MDIyfQ.00xXyY1ZzJ2kK_hA_00wZ_000nQjK9fA0_000wL0")
        String token
) {
}
