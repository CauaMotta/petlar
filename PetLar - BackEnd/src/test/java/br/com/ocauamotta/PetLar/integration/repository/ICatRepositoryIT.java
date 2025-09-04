package br.com.ocauamotta.PetLar.integration.repository;

import br.com.ocauamotta.PetLar.domain.Cat;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.repository.ICatRepository;
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
class ICatRepositoryIT {

    @Autowired
    private ICatRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void test_ShouldPersistAndFindById() {
        Cat saved = repository.insert(cat());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals(AnimalType.CAT, saved.getType());

        Optional<Cat> foundCat = repository.findById(saved.getId());

        assertTrue(foundCat.isPresent());
        assertNotNull(foundCat.get().getId());
        assertEquals("Mimi", foundCat.get().getName());
        assertEquals(AnimalType.CAT, foundCat.get().getType());
    }

    @Test
    void test_ShouldUpdateCat() {
        Cat saved = repository.insert(cat());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals(AnimalType.CAT, saved.getType());

        saved.setName("Luna");

        Cat updated = repository.save(saved);

        assertNotNull(updated.getId());
        assertEquals("Luna", updated.getName());
    }

    @Test
    void test_ShouldDeleteCat() {
        Cat saved = repository.insert(cat());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals(AnimalType.CAT, saved.getType());

        repository.delete(saved);

        Optional<Cat> foundCat = repository.findById(saved.getId());

        assertTrue(foundCat.isEmpty());
    }

    @Test
    void test_ShouldReturnAllCats() {
        repository.save(cat());
        repository.save(cat());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Cat> page = repository.findAll(pageable);

        assertEquals(2, page.getTotalElements());

        List<Cat> list = page.stream().toList();

        for (Cat entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void test_ShouldReturnAllCats_WithStatusAvailable() {
        repository.save(cat());
        repository.save(cat());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Cat> page = repository.findByStatus(AdoptionStatus.AVAILABLE, pageable);

        assertEquals(2, page.getTotalElements());

        List<Cat> list = page.stream().toList();

        for (Cat entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void test_ShouldReturnAllCats_WithStatusAdopted() {
        Cat cat = cat();
        cat.setStatus(AdoptionStatus.ADOPTED);
        repository.save(cat);

        Cat cat2 = cat();
        cat2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(cat2);

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Cat> page = repository.findByStatus(AdoptionStatus.ADOPTED, pageable);

        assertEquals(2, page.getTotalElements());

        List<Cat> list = page.stream().toList();

        for (Cat entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    Cat cat() {
        return Cat.builder()
                .name("Mimi")
                .age(12)
                .type(AnimalType.CAT)
                .breed("SRD")
                .sex(AnimalSex.FEMALE)
                .weight(400)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Carinhosa.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
