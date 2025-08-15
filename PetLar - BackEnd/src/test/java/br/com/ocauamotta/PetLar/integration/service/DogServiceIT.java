package br.com.ocauamotta.PetLar.integration.service;

import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
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
    void save_ShouldPersistDogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());
    }

    @Test
    void update_ShouldUpdatedDogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());

        saved.setName("Totó");

        DogDTO updated = service.update(saved);

        assertNotNull(updated.getId());
        assertEquals("Totó", updated.getName());
    }

    @Test
    void delete_ShouldDeleteDogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());

        service.delete(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> service.findById(saved.getId()));
    }

    @Test
    void findById_ShouldFindADogInDatabase() {
        DogDTO saved = service.save(createDogDTO());

        assertNotNull(saved.getId());
        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());

        DogDTO foundDog = service.findById(saved.getId());

        assertNotNull(foundDog.getId());
        assertEquals(saved.getId(), foundDog.getId());
        assertEquals(saved.getName(), foundDog.getName());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.findById("01"));
    }

    @Test
    void findAll_ShouldReturnAllDogsInDatabase() {
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

    private CreateDogDTO createDogDTO() {
        return new CreateDogDTO(
                "Rex", 3, "Vira-lata", AnimalSex.MALE, 10,
                AnimalSize.MEDIUM, AdoptionStatus.AVAILABLE,
                "Cão amigável", "url.com/img"
        );
    }
}
