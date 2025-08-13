package br.com.ocauamotta.PetLar.domain;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;

import java.time.LocalDate;

public class Animal {

    private String id;
    private String name;
    private Integer yearsOld;
    private AnimalType type;
    private String breed;
    private String sex;
    private Integer weight;
    private AnimalSize size;

    private LocalDate registrationDate;
    private AdoptionStatus status;
    private String description;
}
