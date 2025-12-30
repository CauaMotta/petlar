package br.com.ocauamotta.PetLar.dtos.Animal;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Representa os dados de resposta de um animal.
 * Usado para retornar informações detalhadas ao cliente em operações de consulta.
 */
@Schema(description = "Objeto de resposta contendo informações de um animal")
public record AnimalResponseDto(
        @Schema(description = "Identificador único do animal.")
        String id,
        @Schema(description = "Nome do animal", example = "Luna")
        String name,
        @Schema(description = "Data de nascimento do animal", example = "2025-11-10")
        LocalDate birthDate,
        @Schema(description = "Peso do animal (em gramas)", example = "1200")
        Integer weight,
        @Schema(
                description = "Espécie do animal",
                example = "CACHORRO"
        )
        AnimalType type,
        @Schema(
                description = "Sexo do animal",
                example = "FEMEA"
        )
        AnimalSex sex,
        @Schema(
                description = "Porte do animal",
                example = "MEDIO"
        )
        AnimalSize size,
        @Schema(
                description = "Status de adoção do animal",
                example = "DISPONIVEL"
        )
        AdoptionStatus status,
        @Schema(
                description = "ID do autor do registro do animal",
                example = "69544c..."
        )
        String authorId,
        @Schema(
                description = "Descrição ou observações sobre o animal",
                example = "Luna é uma cadela dócil e brincalhona, vacinada e castrada."
        )
        String description,
        @Schema(
                description = "Data e hora do cadastro do animal",
                example = "2025-12-31T12:05:00.123456-03:00[America/Sao_Paulo]"
        )
        String createdAt,
        @Schema(
                description = "Data e hora da última atualização do animal",
                example = "2025-12-31T12:05:00.123456-03:00[America/Sao_Paulo]"
        )
        String updatedAt
) {
}
