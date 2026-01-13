package br.com.ocauamotta.PetLar.dtos.Adoption;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para capturar a intenção de adoção de um usuário.
 * <p>
 * Contém as validações necessárias para garantir que o ID do animal alvo seja informado
 * e que a justificativa do adotante respeite os limites de tamanho definidos.
 */
@Schema(description = "DTO de Requisição para solicitar a adoção de um animal.")
public record AdoptionRequestDto(
        @Schema(description = "ID do animal que o usuário deseja adotar.", example = "72af45b0...")
        @NotBlank(message = "O ID do animal é obrigatório.")
        String animalId,
        @Schema(description = "Justificativa do porquê o usuário deseja adotar este animal. Deve ter entre 3 e 255 caracteres.",
                example = "Sempre tive cães e procuro um novo amigo para fazer parte da família.")
        @NotNull(message = "A justificativa é obrigatória.")
        @Size(min = 3, max = 255, message = "A justificativa deve conter entre 3 e 255 caracteres.")
        String reason
) {
}
