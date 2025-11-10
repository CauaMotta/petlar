package br.com.ocauamotta.PetLar.dtos;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;

import java.time.LocalDateTime;

public record AnimalResponseDto(
        String id,
        String name,
        Integer age,
        Integer weight,
        AnimalType type,
        AnimalSex sex,
        AnimalSize size,
        LocalDateTime registrationDate,
        AdoptionStatus status,
        String description
) {
}
