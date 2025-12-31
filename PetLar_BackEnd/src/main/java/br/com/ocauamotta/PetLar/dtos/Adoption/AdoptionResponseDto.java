package br.com.ocauamotta.PetLar.dtos.Adoption;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalSummaryDto;
import br.com.ocauamotta.PetLar.dtos.User.UserSummaryDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa a resposta detalhada de um processo de adoção.
 * <p>
 * Utilizado para retornar ao cliente o estado atual da solicitação, incluindo
 * os registros de data e o status do processo.
 */
@Schema(description = "DTO de Resposta com os detalhes de um processo de adoção.")
public record AdoptionResponseDto(
        @Schema(description = "ID único do processo de adoção.", example = "550e8400...")
        String id,
        @Schema(description = "Status atual da adoção.", example = "PENDENTE")
        AdoptionStatus status,
        @Schema(description = "ID do animal que está sendo adotado.")
        AnimalSummaryDto animal,
        @Schema(description = "Autor do animal que está sendo adotado.")
        UserSummaryDto animalOwner,
        @Schema(description = "Usuário que solicitou a adoção.")
        UserSummaryDto adopter,
        @Schema(description = "Motivo/Justificativa fornecida pelo adotante.",
                example = "Tenho um quintal amplo e procuro um companheiro para caminhadas.")
        String reason,
        @Schema(description = "Data de criação da solicitação.", example = "2025-12-31T12:05:00.123456-03:00[America/Sao_Paulo]")
        String createdAt,
        @Schema(description = "Data da última atualização de status.", example = "2025-12-31T12:05:00.123456-03:00[America/Sao_Paulo]")
        String updatedAt
) {
}
