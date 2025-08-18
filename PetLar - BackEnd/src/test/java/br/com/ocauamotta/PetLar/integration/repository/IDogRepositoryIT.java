package br.com.ocauamotta.PetLar.integration.repository;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.repository.IDogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
public class IDogRepositoryIT {

    @Autowired
    private IDogRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void test_ShouldPersistAndFindById() {
        Dog saved = repository.insert(createDog());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());

        Optional<Dog> foundDog = repository.findById(saved.getId());

        assertTrue(foundDog.isPresent());
        assertNotNull(foundDog.get().getId());
        assertEquals("Rex", foundDog.get().getName());
        assertEquals(AnimalType.DOG, foundDog.get().getType());
    }

    @Test
    void test_ShouldUpdateDog() {
        Dog saved = repository.insert(createDog());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());

        saved.setName("Totó");

        Dog updated = repository.save(saved);

        assertNotNull(updated.getId());
        assertEquals("Totó", updated.getName());
    }

    @Test
    void test_ShouldDeleteDog() {
        Dog saved = repository.insert(createDog());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());

        repository.delete(saved);

        Optional<Dog> foundDog = repository.findById(saved.getId());

        assertTrue(foundDog.isEmpty());
    }

    @Test
    void test_ShouldReturnAllDogs() {
        repository.save(createDog());
        repository.save(createDog());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Dog> page = repository.findAll(pageable);

        assertEquals(2, page.getTotalElements());

        List<Dog> list = page.stream().toList();

        for (Dog dog : list) {
            repository.delete(dog);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    Dog createDog() {
        return Dog.builder()
                .name("Rex")
                .age(3)
                .type(AnimalType.DOG)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }
}
