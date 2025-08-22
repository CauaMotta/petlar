package br.com.ocauamotta.PetLar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDogDTO {

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
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

    @NotNull
    private String status;

    @Size(min = 3, max = 250)
    private String description;

    private String urlImage;
}
