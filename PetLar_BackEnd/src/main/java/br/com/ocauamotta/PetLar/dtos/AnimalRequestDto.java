package br.com.ocauamotta.PetLar.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Representa os dados necessários para cadastrar ou atualizar um animal.
 * <p>
 * Este DTO é usado nas operações de criação e atualização de animais, garantindo
 * que todos os campos obrigatórios sejam validados.
 * <p>
 * Em atualizações (PUT), apenas os campos que devem ser modificados precisam ser enviados.
 */
@Schema(description = "Objeto de requisição contendo os dados de entrada para cadastro ou atualização de um animal, em caso de atualização, " +
        "Durante atualizações, apenas os campos desejados podem ser enviados.")
public record AnimalRequestDto(
        @Schema(description = "Nome do animal", example = "Rex")
        @NotBlank
        @Size(min = 3, max = 110, message = "O nome deve conter entre 3 e 110 caracteres.")
        String name,
        @Schema(description = "Data de nascimento do animal", example = "2025-11-10")
        @NotNull
        LocalDate dob,
        @Schema(description = "Peso do animal (em gramas)", example = "1200")
        @NotNull
        @Min(value = 1, message = "O peso deve ser maior que 1.")
        Integer weight,
        @Schema(description = "Espécie do animal", example = "Cachorro")
        @NotBlank
        String type,
        @Schema(description = "Sexo do animal", example = "Macho")
        @NotBlank
        String sex,
        @Schema(description = "Porte do animal", example = "Médio")
        @NotBlank
        String size,
        @Schema(description = "Descrição adicional sobre o animal", example = "Animal dócil, vacinado e sociável.")
        @Size(min = 3, max = 255, message = "A descrição deve conter entre 3 e 255 caracteres.")
        String description
) {
}
