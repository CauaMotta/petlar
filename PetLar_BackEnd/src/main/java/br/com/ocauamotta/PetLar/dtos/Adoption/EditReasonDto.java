package br.com.ocauamotta.PetLar.dtos.Adoption;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para a atualização da justificativa (reason)
 * de uma solicitação de adoção existente.
 */
@Schema(description = "DTO para atualização da justificativa de uma solicitação de adoção.")
public record EditReasonDto(
        @Schema(description = "Nova justificativa para a adoção.", example = "Atualizei a justificativa pois agora tenho mais tempo livre para o pet.")
        @NotNull(message = "A justificativa é obrigatória.")
        @Size(min = 3, max = 255, message = "A justificativa deve conter entre 3 e 255 caracteres.")
        String reason
) {
}
