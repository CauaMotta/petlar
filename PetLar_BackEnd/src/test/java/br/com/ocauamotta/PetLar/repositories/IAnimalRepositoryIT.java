package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Animal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class IAnimalRepositoryIT {

    @Autowired
    private IAnimalRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve buscar uma lista de animais paginada e filtrada por status")
    void testFindByStatus() {
        repository.insert(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO));

        Page<Animal> page = repository.findByStatus(AdoptionStatus.DISPONIVEL, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals(AdoptionStatus.DISPONIVEL, page.getContent().get(0).getStatus());
    }

    @Test
    @DisplayName("Deve buscar uma lista de animais paginada, filtrada por status e espécie")
    void testFindByStatusAndType() {
        repository.insert(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Miau", AnimalType.GATO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO));

        Page<Animal> page = repository.findByStatusAndType(AdoptionStatus.DISPONIVEL, AnimalType.GATO, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals(AdoptionStatus.DISPONIVEL, page.getContent().get(0).getStatus());
        assertEquals(AnimalType.GATO, page.getContent().get(0).getType());
    }

    Animal createAnimal(String name, AnimalType type, AnimalSex sex, AdoptionStatus status) {
        return Animal.builder()
                .name(name)
                .dob(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(type)
                .sex(sex)
                .size(AnimalSize.PEQUENO)
                .registrationDate(LocalDateTime.of(2025, 10, 10, 12, 00, 00))
                .status(status)
                .description("Animal docil")
                .build();
    }
}