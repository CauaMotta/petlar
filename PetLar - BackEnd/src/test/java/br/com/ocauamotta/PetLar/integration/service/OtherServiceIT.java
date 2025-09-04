package br.com.ocauamotta.PetLar.integration.service;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.service.OtherService;
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
@Import(OtherService.class)
class OtherServiceIT {

    @Autowired
    private OtherService service;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("Others");
    }

    @Test
    void testSave_ShouldPersistOtherInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals("Outro", saved.getType());
    }

    @Test
    void testUpdate_ShouldUpdateOtherInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals("Outro", saved.getType());

        saved.setName("Luna");

        AnimalDTO updated = service.update(saved);

        assertNotNull(updated.getId());
        assertEquals("Luna", updated.getName());
    }

    @Test
    void testDelete_ShouldDeleteOtherInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals("Outro", saved.getType());

        service.delete(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> service.findById(saved.getId()));
    }

    @Test
    void testFindById_ShouldFindOtherInDatabase() {
        AnimalDTO saved = service.save(createAnimalDTO());

        assertNotNull(saved.getId());
        assertEquals("Bolt", saved.getName());
        assertEquals("Outro", saved.getType());

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
    void testFindAll_ShouldReturnAllOthersInDatabase() {
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
    void testFindAll_ShouldReturnAllOthers_WithStatusAvailable() {
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
    void testFindAll_ShouldReturnAllOthers_WithStatusAdopted() {
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
                .name("Bolt")
                .age(48)
                .breed("Coelho")
                .sex(AnimalSex.MALE.getLabel())
                .weight(250)
                .size(AnimalSize.MEDIUM.getLabel())
                .description("Animal dócil e brincalhão.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
