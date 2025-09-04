package br.com.ocauamotta.PetLar.mapper;

import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import org.springframework.stereotype.Component;

@Component
public class OtherMapper {

    private OtherMapper() {
    }

    public static Other toEntity(AnimalDTO dto) {
        if (dto == null) return null;

        return Other.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .type(AnimalType.fromLabel(dto.getType()))
                .breed(dto.getBreed())
                .sex(AnimalSex.fromLabel(dto.getSex()))
                .weight(dto.getWeight())
                .size(AnimalSize.fromLabel(dto.getSize()))
                .registrationDate(dto.getRegistrationDate())
                .status(AdoptionStatus.fromLabel(dto.getStatus()))
                .description(dto.getDescription())
                .urlImage(dto.getUrlImage())
                .author(dto.getAuthor())
                .phone(dto.getPhone())
                .build();
    }

    public static AnimalDTO toDTO(Other entity) {
        if (entity == null) return null;

        return AnimalDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(entity.getAge())
                .type(entity.getType().getLabel())
                .breed(entity.getBreed())
                .sex(entity.getSex().getLabel())
                .weight(entity.getWeight())
                .size(entity.getSize().getLabel())
                .registrationDate(entity.getRegistrationDate())
                .status(entity.getStatus().getLabel())
                .description(entity.getDescription())
                .urlImage(entity.getUrlImage())
                .author(entity.getAuthor())
                .phone(entity.getPhone())
                .build();
    }

    public static Other create(CreateAnimalDTO createDTO) {
        if (createDTO == null) return null;

        return Other.builder()
                .name(createDTO.getName())
                .age(createDTO.getAge())
                .breed(createDTO.getBreed())
                .sex(AnimalSex.fromLabel(createDTO.getSex()))
                .weight(createDTO.getWeight())
                .size(AnimalSize.fromLabel(createDTO.getSize()))
                .description(createDTO.getDescription())
                .urlImage(createDTO.getUrlImage())
                .author(createDTO.getAuthor())
                .phone(createDTO.getPhone())
                .build();
    }
}
