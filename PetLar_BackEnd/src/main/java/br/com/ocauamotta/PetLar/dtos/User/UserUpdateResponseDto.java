package br.com.ocauamotta.PetLar.dtos.User;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO utilizado para encapsular a resposta de uma
 * operação de atualização de dados do usuário.
 * <p>
 * Inclui o DTO de resposta do usuário e, opcionalmente, um novo token JWT
 * caso o e-mail tenha sido alterado.
 */
@Schema(hidden = true)
public record UserUpdateResponseDto(UserResponseDto userResponseDto, String newToken) {
}
