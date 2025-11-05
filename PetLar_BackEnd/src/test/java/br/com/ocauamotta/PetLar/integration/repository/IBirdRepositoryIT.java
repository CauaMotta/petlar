package br.com.ocauamotta.PetLar.integration.repository;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.repository.IBirdRepository;
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
class IBirdRepositoryIT {

    @Autowired
    private IBirdRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void test_ShouldPersistAndFindById() {
        Bird saved = repository.insert(bird());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals(AnimalType.BIRD, saved.getType());

        Optional<Bird> foundBird = repository.findById(saved.getId());

        assertTrue(foundBird.isPresent());
        assertNotNull(foundBird.get().getId());
        assertEquals("Loro", foundBird.get().getName());
        assertEquals(AnimalType.BIRD, foundBird.get().getType());
    }

    @Test
    void test_ShouldUpdateBird() {
        Bird saved = repository.insert(bird());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals(AnimalType.BIRD, saved.getType());

        saved.setName("José");

        Bird updated = repository.save(saved);

        assertNotNull(updated.getId());
        assertEquals("José", updated.getName());
    }

    @Test
    void test_ShouldDeleteBird() {
        Bird saved = repository.insert(bird());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals(AnimalType.BIRD, saved.getType());

        repository.delete(saved);

        Optional<Bird> foundBird = repository.findById(saved.getId());

        assertTrue(foundBird.isEmpty());
    }

    @Test
    void test_ShouldReturnAllBirds() {
        repository.save(bird());
        repository.save(bird());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Bird> page = repository.findAll(pageable);

        assertEquals(2, page.getTotalElements());

        List<Bird> list = page.stream().toList();

        for (Bird entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void test_ShouldReturnAllBirds_WithStatusAvailable() {
        repository.save(bird());
        repository.save(bird());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Bird> page = repository.findByStatus(AdoptionStatus.AVAILABLE, pageable);

        assertEquals(2, page.getTotalElements());

        List<Bird> list = page.stream().toList();

        for (Bird entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void test_ShouldReturnAllBirds_WithStatusAdopted() {
        Bird bird = bird();
        bird.setStatus(AdoptionStatus.ADOPTED);
        repository.save(bird);

        Bird bird2 = bird();
        bird2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(bird2);

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Bird> page = repository.findByStatus(AdoptionStatus.ADOPTED, pageable);

        assertEquals(2, page.getTotalElements());

        List<Bird> list = page.stream().toList();

        for (Bird entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    Bird bird() {
        return Bird.builder()
                .name("Loro")
                .age(12)
                .type(AnimalType.BIRD)
                .breed("Papagaio")
                .sex(AnimalSex.MALE)
                .weight(50)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Pássaro alegre e falador.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
