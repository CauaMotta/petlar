package br.com.ocauamotta.PetLar.dtos.User;

/**
 * DTO utilizado para retornar informações do usuário após as operações.
 * <p>
 * O DTO de resposta expõe apenas os dados não sensíveis, garantindo a segurança ao evitar a exposição da senha.
 */
public record UserResponseDto(String id, String email) {
}
