package br.com.ocauamotta.PetLar.integration.repository;

import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.repository.IOtherRepository;
import br.com.ocauamotta.PetLar.repository.IOtherRepository;
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
class IOtherRepositoryIT {

    @Autowired
    private IOtherRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void test_ShouldPersistAndFindById() {
        Other saved = repository.insert(other());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals(AnimalType.OTHER, saved.getType());

        Optional<Other> foundOther = repository.findById(saved.getId());

        assertTrue(foundOther.isPresent());
        assertNotNull(foundOther.get().getId());
        assertEquals("Bolt", foundOther.get().getName());
        assertEquals(AnimalType.OTHER, foundOther.get().getType());
    }

    @Test
    void test_ShouldUpdateOther() {
        Other saved = repository.insert(other());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals(AnimalType.OTHER, saved.getType());

        saved.setName("Pernalonga");

        Other updated = repository.save(saved);

        assertNotNull(updated.getId());
        assertEquals("Pernalonga", updated.getName());
    }

    @Test
    void test_ShouldDeleteOther() {
        Other saved = repository.insert(other());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals(AnimalType.OTHER, saved.getType());

        repository.delete(saved);

        Optional<Other> foundOther = repository.findById(saved.getId());

        assertTrue(foundOther.isEmpty());
    }

    @Test
    void test_ShouldReturnAllOthers() {
        repository.save(other());
        repository.save(other());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Other> page = repository.findAll(pageable);

        assertEquals(2, page.getTotalElements());

        List<Other> list = page.stream().toList();

        for (Other entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void test_ShouldReturnAllOthers_WithStatusAvailable() {
        repository.save(other());
        repository.save(other());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Other> page = repository.findByStatus(AdoptionStatus.AVAILABLE, pageable);

        assertEquals(2, page.getTotalElements());

        List<Other> list = page.stream().toList();

        for (Other entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void test_ShouldReturnAllOthers_WithStatusAdopted() {
        Other other = other();
        other.setStatus(AdoptionStatus.ADOPTED);
        repository.save(other);

        Other other2 = other();
        other2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(other2);

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Other> page = repository.findByStatus(AdoptionStatus.ADOPTED, pageable);

        assertEquals(2, page.getTotalElements());

        List<Other> list = page.stream().toList();

        for (Other entity : list) {
            repository.delete(entity);
        }

        page = repository.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    Other other() {
        return Other.builder()
                .name("Bolt")
                .age(48)
                .type(AnimalType.OTHER)
                .breed("Coelho")
                .sex(AnimalSex.MALE)
                .weight(250)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Animal dócil e brincalhão.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
