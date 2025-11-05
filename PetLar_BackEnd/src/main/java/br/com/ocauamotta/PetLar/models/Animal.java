package br.com.ocauamotta.PetLar.models;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

    @Id
    private String id;

    @Size(min = 3, max = 255, message = "O nome deve conter entre 1 e 255 caracteres.")
    private String name;

    @Size(min = 1, message = "A idade deve ser maior do que 1.")
    private Integer age;

    @Size(min = 1, message = "O peso deve ser maior do que 1.")
    private Integer weight;

    @NotNull
    private AnimalType type;

    @NotNull
    private AnimalSex sex;

    @NotNull
    private AnimalSize size;

    @NotNull
    private ZonedDateTime registrationDate;

    @NotNull
    private AdoptionStatus status;

    private String description;
}
