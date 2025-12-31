package br.com.ocauamotta.PetLar.dtos.User;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO simplificado contendo apenas informações básicas de identificação do usuário.
 * <p>
 * Utilizado em listagens e como objeto aninhado em outros DTOs para reduzir a carga de dados.
 */
@Schema(description = "Resumo das informações de um usuário.")
public record UserSummaryDto(
        @Schema(description = "ID do usuário.", example = "10af45b0...")
        String id,
        @Schema(description = "Nome do usuário.", example = "João da Silva")
        String name
) {
}
