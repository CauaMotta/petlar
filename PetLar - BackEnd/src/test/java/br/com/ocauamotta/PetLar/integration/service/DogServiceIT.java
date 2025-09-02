package br.com.ocauamotta.PetLar.integration.service;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.service.DogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(DogService.class)
class DogServiceIT {

    @Autowired
    private DogService service;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("Dogs");
    }

    @Test
    void testSave_ShouldPersistDogInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());
    }

    @Test
    void testUpdate_ShouldUpdateDogInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());

        saved.setName("Totó");

        AnimalDTO updated = service.update(saved);

        assertNotNull(updated.getId());
        assertEquals("Totó", updated.getName());
    }

    @Test
    void testDelete_ShouldDeleteDogInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());

        service.delete(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> service.findById(saved.getId()));
    }

    @Test
    void testFindById_ShouldFindDogInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());

        AnimalDTO foundDog = service.findById(saved.getId());

        assertNotNull(foundDog.getId());
        assertEquals(saved.getId(), foundDog.getId());
        assertEquals(saved.getName(), foundDog.getName());
    }

    @Test
    void testFindById_ShouldThrowException_WhenNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.findById("01"));
    }

    @Test
    void testFindAll_ShouldReturnAllDogsInDatabase() {
        service.save(createAnimalDTO());
        service.save(createAnimalDTO());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<AnimalDTO> page = service.findAll(pageable, null);

        assertEquals(2, page.getTotalElements());

        List<AnimalDTO> list = page.stream().toList();

        for (AnimalDTO dog : list) {
            service.delete(dog.getId());
        }

        page = service.findAll(pageable, null);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindAll_ShouldReturnAllDogs_WithStatusAvailable() {
        service.save(createAnimalDTO());
        service.save(createAnimalDTO());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<AnimalDTO> page = service.findAll(pageable, "Disponível");

        assertEquals(2, page.getTotalElements());

        List<AnimalDTO> list = page.stream().toList();

        for (AnimalDTO dog : list) {
            service.delete(dog.getId());
        }

        page = service.findAll(pageable, "Disponível");

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindAll_ShouldReturnAllDogs_WithStatusAdopted() {
        AnimalDTO dog1 = service.save(createAnimalDTO());
        AnimalDTO dog2 = service.save(createAnimalDTO());

        dog1.setStatus(AdoptionStatus.ADOPTED.getLabel());
        dog2.setStatus(AdoptionStatus.ADOPTED.getLabel());

        service.update(dog1);
        service.update(dog2);

        PageRequest pageable = PageRequest.of(0, 2);
        Page<AnimalDTO> page = service.findAll(pageable, "Adotado");

        assertEquals(2, page.getTotalElements());

        List<AnimalDTO> list = page.stream().toList();

        for (AnimalDTO dog : list) {
            service.delete(dog.getId());
        }

        page = service.findAll(pageable, "Adotado");

        assertEquals(0, page.getTotalElements());
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Rex")
                .age(3)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE.getLabel())
                .weight(10)
                .size(AnimalSize.MEDIUM.getLabel())
                .description("Cão amigável")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
