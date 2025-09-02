package br.com.ocauamotta.PetLar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAnimalDTO {

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Min(value = 1, message = "A idade deve ser maior que zero")
    private Integer age;

    @NotNull
    @Size(min = 3, max = 100)
    private String breed;

    @NotNull
    private String sex;

    @NotNull
    private Integer weight;

    @NotNull
    private String size;

    @Size(min = 3, max = 250)
    private String description;

    private String urlImage;
}
