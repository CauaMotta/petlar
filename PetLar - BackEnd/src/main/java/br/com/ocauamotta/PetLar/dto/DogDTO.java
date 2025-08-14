package br.com.ocauamotta.PetLar.dto;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogDTO {

    @NotNull
    private String id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    private Integer yearsOld;

    @NotNull
    private AnimalType type;

    @NotNull
    @Size(min = 3, max = 100)
    private String breed;

    @NotNull
    private AnimalSex sex;

    @NotNull
    private Integer weight;

    @NotNull
    private AnimalSize size;

    @NotNull
    private LocalDate registrationDate;

    @NotNull
    private AdoptionStatus status;

    @Size(min = 3, max = 250)
    private String description;

    private String urlImage;
}
