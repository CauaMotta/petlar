package br.com.ocauamotta.PetLar.integration.service;

import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
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
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());
    }

    @Test
    void testUpdate_ShouldUpdateDogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());

        saved.setName("Totó");

        DogDTO updated = service.update(saved);

        assertNotNull(updated.getId());
        assertEquals("Totó", updated.getName());
    }

    @Test
    void testDelete_ShouldDeleteDogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());

        service.delete(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> service.findById(saved.getId()));
    }

    @Test
    void testFindById_ShouldFindDogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());

        DogDTO foundDog = service.findById(saved.getId());

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
        service.save(createDogDTO());
        service.save(createDogDTO());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<DogDTO> page = service.findAll(pageable);

        assertEquals(2, page.getTotalElements());

        List<DogDTO> list = page.stream().toList();

        for (DogDTO dog : list) {
            service.delete(dog.getId());
        }

        page = service.findAll(pageable);

        assertEquals(0, page.getTotalElements());
    }

    CreateDogDTO createDogDTO() {
        return CreateDogDTO.builder()
                .name("Rex")
                .age(3)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE.getLabel())
                .weight(10)
                .size(AnimalSize.MEDIUM.getLabel())
                .status(AdoptionStatus.AVAILABLE.getLabel())
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }
}
