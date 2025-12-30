package br.com.ocauamotta.PetLar.dtos.Animal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Representa os dados necessários para cadastrar ou atualizar um animal.
 * <p>
 * Este DTO é usado nas operações de criação e atualização de animais, garantindo
 * que todos os campos obrigatórios sejam validados.
 */
@Schema(description = "Objeto de requisição contendo os dados de entrada para cadastro ou atualização de um animal, em caso de atualização, " +
        "Durante atualizações, apenas os campos desejados podem ser enviados.")
public record AnimalRequestDto(
        @Schema(description = "Nome do animal", example = "Luna")
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 110, message = "O nome deve conter entre 3 e 110 caracteres.")
        String name,
        @Schema(description = "Data de nascimento do animal", example = "2025-11-10")
        @NotNull(message = "A data de nascimento é obrigatória.")
        @PastOrPresent(message = "A data de nascimento deve ser igual ou anterior à data atual")
        LocalDate dob,
        @Schema(description = "Peso do animal (em gramas)", example = "1200")
        @NotNull(message = "O peso é obrigatório.")
        @Min(value = 1, message = "O peso deve ser maior que zero.")
        Integer weight,
        @Schema(description = "Espécie do animal", example = "Cachorro")
        @NotBlank(message = "A espécie do animal é obrigatória.")
        String type,
        @Schema(description = "Sexo do animal", example = "Femea")
        @NotBlank(message = "O sexo do animal é obrigatório.")
        String sex,
        @Schema(description = "Porte do animal", example = "Medio")
        @NotBlank(message = "O porte do animal é obrigatório.")
        String size,
        @Schema(description = "Descrição adicional sobre o animal", example = "Luna é uma cadela dócil e brincalhona, vacinada e castrada.")
        @Size(min = 3, max = 255, message = "A descrição deve conter entre 3 e 255 caracteres.")
        String description
) {
}
