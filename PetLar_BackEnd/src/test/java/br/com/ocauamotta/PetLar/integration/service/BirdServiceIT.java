package br.com.ocauamotta.PetLar.integration.service;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.service.BirdService;
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
@Import(BirdService.class)
class BirdServiceIT {

    @Autowired
    private BirdService service;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("Birds");
    }

    @Test
    void testSave_ShouldPersistBirdInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals("Ave", saved.getType());
    }

    @Test
    void testUpdate_ShouldUpdateBirdInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals("Ave", saved.getType());

        saved.setName("Luna");

        AnimalDTO updated = service.update(saved);

        assertNotNull(updated.getId());
        assertEquals("Luna", updated.getName());
    }

    @Test
    void testDelete_ShouldDeleteBirdInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals("Ave", saved.getType());

        service.delete(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> service.findById(saved.getId()));
    }

    @Test
    void testFindById_ShouldFindBirdInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Loro", saved.getName());
        assertEquals("Ave", saved.getType());

        AnimalDTO foundCat = service.findById(saved.getId());

        assertNotNull(foundCat.getId());
        assertEquals(saved.getId(), foundCat.getId());
        assertEquals(saved.getName(), foundCat.getName());
    }

    @Test
    void testFindById_ShouldThrowException_WhenNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.findById("01"));
    }

    @Test
    void testFindAll_ShouldReturnAllBirdsInDatabase() {
        service.save(createAnimalDTO());
        service.save(createAnimalDTO());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<AnimalDTO> page = service.findAll(pageable, null);

        assertEquals(2, page.getTotalElements());

        List<AnimalDTO> list = page.stream().toList();

        for (AnimalDTO entity : list) {
            service.delete(entity.getId());
        }

        page = service.findAll(pageable, null);

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindAll_ShouldReturnAllBirds_WithStatusAvailable() {
        service.save(createAnimalDTO());
        service.save(createAnimalDTO());

        PageRequest pageable = PageRequest.of(0, 2);
        Page<AnimalDTO> page = service.findAll(pageable, "Disponível");

        assertEquals(2, page.getTotalElements());

        List<AnimalDTO> list = page.stream().toList();

        for (AnimalDTO entity : list) {
            service.delete(entity.getId());
        }

        page = service.findAll(pageable, "Disponível");

        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindAll_ShouldReturnAllBirds_WithStatusAdopted() {
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

        for (AnimalDTO entity : list) {
            service.delete(entity.getId());
        }

        page = service.findAll(pageable, "Adotado");

        assertEquals(0, page.getTotalElements());
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Loro")
                .age(12)
                .breed("SRD")
                .sex(AnimalSex.FEMALE.getLabel())
                .weight(400)
                .size(AnimalSize.SMALL.getLabel())
                .description("Carinhosa.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
