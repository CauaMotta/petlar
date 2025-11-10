package br.com.ocauamotta.PetLar.mappers;

import br.com.ocauamotta.PetLar.dtos.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Animal;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    private AnimalMapper() {
    }

    public static Animal toEntity(AnimalResponseDto dto) {
        if (dto == null) return null;

        return Animal.builder()
                .id(dto.id())
                .name(dto.name())
                .age(dto.age())
                .weight(dto.weight())
                .type(dto.type())
                .sex(dto.sex())
                .size(dto.size())
                .registrationDate(dto.registrationDate())
                .status(dto.status())
                .description(dto.description())
                .build();
    }

    public static AnimalResponseDto toDTO(Animal entity) {
        if (entity == null) return null;

        return new AnimalResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getAge(),
                entity.getWeight(),
                entity.getType(),
                entity.getSex(),
                entity.getSize(),
                entity.getRegistrationDate(),
                entity.getStatus(),
                entity.getDescription()
        );
    }

    public static Animal createEntity(AnimalRequestDto dto) {
        if (dto == null) return null;

        return Animal.builder()
                .name(dto.name())
                .age(dto.age())
                .weight(dto.weight())
                .type(AnimalType.fromString(dto.type()))
                .sex(AnimalSex.fromString(dto.sex()))
                .size(AnimalSize.fromString(dto.size()))
                .description(dto.description())
                .build();
    }
}
