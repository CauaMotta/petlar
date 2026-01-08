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
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    @DisplayName("Deve buscar uma lista de animais paginada e filtrada por status.")
    void testFindByStatus() {
        repository.insert(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO));

        Page<Animal> page = repository.findByStatus(AdoptionStatus.DISPONIVEL, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals(AdoptionStatus.DISPONIVEL, page.getContent().get(0).getStatus());
    }

    @Test
    @DisplayName("Deve buscar uma lista de animais paginada, filtrada por status e espécie.")
    void testFindByStatusAndType() {
        repository.insert(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Miau", AnimalType.GATO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO));

        Page<Animal> page = repository.findByStatusAndType(AdoptionStatus.DISPONIVEL, AnimalType.GATO, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals(AdoptionStatus.DISPONIVEL, page.getContent().get(0).getStatus());
        assertEquals(AnimalType.GATO, page.getContent().get(0).getType());
    }

    @Test
    @DisplayName("Deve buscar uma lista de animais paginada e filtrada pelo ID do autor.")
    void testFindByAuthorId() {
        repository.insert(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO));

        Page<Animal> page = repository.findByAuthorId("1", PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
    }

    Animal createAnimal(String name, AnimalType type, AnimalSex sex, AdoptionStatus status) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return Animal.builder()
                .name(name)
                .birthDate(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(type)
                .sex(sex)
                .size(AnimalSize.PEQUENO)
                .status(status)
                .authorId("1")
                .description("Animal docil")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}