package br.com.ocauamotta.PetLar.dtos.Auth;

/**
 * DTO utilizado como resposta ao realizar uma autenticação na API.
 * Este record encapsula o token gerado pelo serviço de segurança.
 *
 * @param token A {@code String} que contém o token (JWT) assinado.
 */
public record TokenResponse(String token) {
}
