package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.models.Adoption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class IAdoptionRepositoryIT {

    @Autowired
    private IAdoptionRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve buscar uma página de registros de adoção associados a um adotante específico.")
    void testFindByAdopterId() {
        repository.insert(createAdoption());

        Page<Adoption> page = repository.findByAdopterId("3", PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar uma página de registros de adoção associados ao autor dos animais.")
    void testFindByAnimalOwnerId() {
        repository.insert(createAdoption());

        Page<Adoption> page = repository.findByAnimalOwnerId("2", PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
    }

    Adoption createAdoption() {
        return Adoption.builder()
                .status(AdoptionStatus.PENDENTE)
                .animalId("1")
                .animalOwnerId("2")
                .adopterId("3")
                .build();
    }
}