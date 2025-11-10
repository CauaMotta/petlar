package br.com.ocauamotta.PetLar.models;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Animals")
public class Animal {
    @Id
    private String id;
    private String name;
    private Integer age;
    private Integer weight;
    private AnimalType type;
    private AnimalSex sex;
    private AnimalSize size;
    private LocalDateTime registrationDate;
    private AdoptionStatus status;
    private String description;
}
