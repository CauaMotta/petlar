package br.com.ocauamotta.PetLar.domain;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Animal {

    @Id
    private String id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    private Integer age;

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
