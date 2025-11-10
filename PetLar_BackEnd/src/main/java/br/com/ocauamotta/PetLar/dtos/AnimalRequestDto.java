package br.com.ocauamotta.PetLar.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnimalRequestDto(
        @NotBlank
        @Size(min = 3, max = 255, message = "O nome deve conter entre 1 e 255 caracteres.")
        String name,
        @NotNull
        @Min(value = 1, message = "A idade deve ser superior a 1.")
        Integer age,
        @NotNull
        @Min(value = 1, message = "O peso deve ser maior do que 1.")
        Integer weight,
        @NotBlank
        String type,
        @NotBlank
        String sex,
        @NotBlank
        String size,
        @Size(min = 3, max = 255, message = "A descrição deve conter entre 3 e 255 caracteres.")
        String description
) {
}
