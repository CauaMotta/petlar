package br.com.ocauamotta.PetLar.dtos.User;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO utilizado para retornar informações do usuário após as operações.
 * <p>
 * O DTO de resposta expõe apenas os dados não sensíveis, garantindo a segurança ao evitar a exposição da senha.
 */
@Schema(description = "DTO de Resposta que expõe os dados públicos do usuário.")
public record UserResponseDto(
        @Schema(description = "ID único do usuário.", example = "10af45b0-9e8c-4c7d-8f2a-8c5e6d0f1b3a")
        String id,
        @Schema(description = "Endereço de e-mail do usuário.", example = "usuario@petlar.com.br")
        String email,
        @Schema(description = "Nome do usuário.", example = "João da Silva")
        String name,
        @Schema(description = "Data e hora de criação do registro.", example = "2025-11-10T10:00:00.123456789-03:00[America/Sao_Paulo]")
        String createdAt,
        @Schema(description = "Data e hora da última atualização do registro.", example = "2025-11-10T10:00:00.123456789-03:00[America/Sao_Paulo]")
        String updatedAt,
        @Schema(description = "Data e hora da exclusão lógica do registro. É null se o usuário não foi excluído.", nullable = true, example = "null")
        String deletedAt
) {
}
