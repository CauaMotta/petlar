package br.com.ocauamotta.PetLar.mapper;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import org.springframework.stereotype.Component;

@Component
public class DogMapper {

    private DogMapper() {
    }

    /**
     * Convert DogDTO to Dog
     */
    public static Dog toDog(DogDTO dto) {
        if (dto == null) return null;

        return Dog.builder()
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
                .build();
    }

    /**
     * Convert Dog to DogDTO
     */
    public static DogDTO toDTO(Dog dog) {
        if (dog == null) return null;

        return DogDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .type(dog.getType().getLabel())
                .breed(dog.getBreed())
                .sex(dog.getSex().getLabel())
                .weight(dog.getWeight())
                .size(dog.getSize().getLabel())
                .registrationDate(dog.getRegistrationDate())
                .status(dog.getStatus().getLabel())
                .description(dog.getDescription())
                .urlImage(dog.getUrlImage())
                .build();
    }

    /**
     * Create dog
     */
    public static Dog create(CreateDogDTO createDogDTO) {
        if (createDogDTO == null) return null;

        return Dog.builder()
                .name(createDogDTO.getName())
                .age(createDogDTO.getAge())
                .breed(createDogDTO.getBreed())
                .sex(AnimalSex.fromLabel(createDogDTO.getSex()))
                .weight(createDogDTO.getWeight())
                .size(AnimalSize.fromLabel(createDogDTO.getSize()))
                .status(AdoptionStatus.fromLabel(createDogDTO.getStatus()))
                .description(createDogDTO.getDescription())
                .urlImage(createDogDTO.getUrlImage())
                .build();
    }
}
