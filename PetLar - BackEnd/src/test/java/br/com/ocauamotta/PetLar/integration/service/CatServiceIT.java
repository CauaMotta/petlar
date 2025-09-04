package br.com.ocauamotta.PetLar.integration.service;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.service.CatService;
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
@Import(CatService.class)
class CatServiceIT {

    @Autowired
    private CatService service;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("Cats");
    }

    @Test
    void testSave_ShouldPersistCatInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals("Gato", saved.getType());
    }

    @Test
    void testUpdate_ShouldUpdateCatInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals("Gato", saved.getType());

        saved.setName("Luna");

        AnimalDTO updated = service.update(saved);

        assertNotNull(updated.getId());
        assertEquals("Luna", updated.getName());
    }

    @Test
    void testDelete_ShouldDeleteCatInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals("Gato", saved.getType());

        service.delete(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> service.findById(saved.getId()));
    }

    @Test
    void testFindById_ShouldFindCatInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Mimi", saved.getName());
        assertEquals("Gato", saved.getType());

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
    void testFindAll_ShouldReturnAllCatsInDatabase() {
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
    void testFindAll_ShouldReturnAllCats_WithStatusAvailable() {
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
    void testFindAll_ShouldReturnAllCats_WithStatusAdopted() {
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
                .name("Mimi")
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
