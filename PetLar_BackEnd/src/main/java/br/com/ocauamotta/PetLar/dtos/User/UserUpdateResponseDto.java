package br.com.ocauamotta.PetLar.dtos.User;

/**
 * DTO utilizado para encapsular a resposta de uma
 * operação de atualização de dados do usuário.
 * <p>
 * Inclui o DTO de resposta do usuário e, opcionalmente, um novo token JWT
 * caso o e-mail tenha sido alterado.
 */
public record UserUpdateResponseDto(UserResponseDto userResponseDto, String newToken) {
}
