package br.com.ocauamotta.PetLar.dtos.Animal;

import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO resumido para exibição de informações básicas de um animal.
 */
@Schema(description = "DTO que representa um resumo das informações de um animal.")
public record AnimalSummaryDto(
        @Schema(description = "Identificador único do animal.", example = "550e8400...")
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
                description = "Descrição ou observações sobre o animal",
                example = "Luna é uma cadela dócil e brincalhona, vacinada e castrada."
        )
        String description
) {
}
