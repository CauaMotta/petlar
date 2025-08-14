package br.com.ocauamotta.PetLar.mapper;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
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
                .yearsOld(dto.getYearsOld())
                .type(dto.getType())
                .breed(dto.getBreed())
                .sex(dto.getSex())
                .weight(dto.getWeight())
                .size(dto.getSize())
                .registrationDate(dto.getRegistrationDate())
                .status(dto.getStatus())
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
                .yearsOld(dog.getYearsOld())
                .type(dog.getType())
                .breed(dog.getBreed())
                .sex(dog.getSex())
                .weight(dog.getWeight())
                .size(dog.getSize())
                .registrationDate(dog.getRegistrationDate())
                .status(dog.getStatus())
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
                .yearsOld(createDogDTO.getYearsOld())
                .breed(createDogDTO.getBreed())
                .sex(createDogDTO.getSex())
                .weight(createDogDTO.getWeight())
                .size(createDogDTO.getSize())
                .status(createDogDTO.getStatus())
                .description(createDogDTO.getDescription())
                .urlImage(createDogDTO.getUrlImage())
                .build();
    }
}
