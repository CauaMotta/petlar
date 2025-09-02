package br.com.ocauamotta.PetLar.domain;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@SuperBuilder
@Document(collection = "Dogs")
public class Dog extends Animal{

    public Dog(String id, String name, Integer age, AnimalType type, String breed, AnimalSex sex, Integer weight, AnimalSize size, LocalDate registrationDate, AdoptionStatus status, String description, String urlImage, String author, String phone) {
        super(id, name, age, type, breed, sex, weight, size, registrationDate, status, description, urlImage, author, phone);
    }

    public Dog() {
    }
}

