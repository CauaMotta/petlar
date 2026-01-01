package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.models.Animal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnimalServiceIT {

    @Autowired
    private AnimalService service;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("Animals");
    }

    @Test
    @DisplayName("Deve salvar um novo animal e busca-lo no banco de dados com sucesso")
    void testSaveAndFindById_ShouldSaveAndFindAnAnimal() {
        AnimalResponseDto savedAnimal = service.save(createAnimalRequestDto("Rex", "Cachorro", "Macho"));

        assertNotNull(savedAnimal.id());

        AnimalResponseDto foundAnimal = service.findById(savedAnimal.id());

        assertEquals(savedAnimal.name(), foundAnimal.name());
        assertEquals(savedAnimal.status(), foundAnimal.status());
    }

    @Test
    @DisplayName("Deve atualizar um animal com sucesso")
    void testUpdate_ShouldUpdateAnAnimal() {
        AnimalResponseDto savedAnimal = service.save(createAnimalRequestDto("Rex", "Cachorro", "Macho"));

        AnimalResponseDto updatedAnimal = service.update(savedAnimal.id(), createAnimalRequestDto("Lua", "Gato", "Femea"));

        assertNotNull(updatedAnimal);
        assertEquals(savedAnimal.id(), updatedAnimal.id());
        assertEquals("Lua", updatedAnimal.name());
        assertEquals(AnimalType.GATO, updatedAnimal.type());
    }

    @Test
    @DisplayName("Deve deletar um animal com sucesso")
    void testDelete_ShouldDeleteAnAnimal() {
        AnimalResponseDto savedAnimal = service.save(createAnimalRequestDto("Rex", "Cachorro", "Macho"));

        service.delete(savedAnimal.id());

        assertThrows(EntityNotFoundException.class, () -> service.findById(savedAnimal.id()));
    }

    @Test
    @DisplayName("Deve buscar todos os animais sem filtros")
    void testFindAll_ShouldReturnAllAnimals() {
        service.save(createAnimalRequestDto("Rex", "Cachorro", "Macho"));
        service.save(createAnimalRequestDto("Bob", "Cachorro", "Macho"));
        service.save(createAnimalRequestDto("Lua", "Gato", "Femea"));
        service.save(createAnimalRequestDto("Miau", "Gato", "Macho"));

        Page<AnimalResponseDto> page = service.findAll(PageRequest.of(0, 10), "disponivel", null);

        assertEquals(4, page.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar todos os animais com filtro de espécie")
    void testFindAll_ShouldReturnAllAnimalsWithTypeFilter() {
        service.save(createAnimalRequestDto("Rex", "Cachorro", "Macho"));
        service.save(createAnimalRequestDto("Bob", "Cachorro", "Macho"));
        service.save(createAnimalRequestDto("Lua", "Gato", "Femea"));
        service.save(createAnimalRequestDto("Miau", "Gato", "Macho"));

        Page<AnimalResponseDto> page = service.findAll(PageRequest.of(0, 10), "disponivel", "gato");

        assertEquals(2, page.getTotalElements());
        assertEquals(AnimalType.GATO, page.getContent().get(0).type());
    }

    @Test
    @DisplayName("Deve buscar todos os animais com filtro de status da adoção")
    void testFindAll_ShouldReturnAllAnimalsWithStatusFilter() {
        mongoTemplate.save(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.ADOTADO), "Animals");
        mongoTemplate.save(createAnimal("Bob", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL), "Animals");
        mongoTemplate.save(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO), "Animals");
        mongoTemplate.save(createAnimal("Miau", AnimalType.GATO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL), "Animals");

        Page<AnimalResponseDto> page = service.findAll(PageRequest.of(0, 10), "adotado", null);

        assertEquals(2, page.getTotalElements());
        assertEquals(AdoptionStatus.ADOTADO, page.getContent().get(0).status());
    }

    AnimalRequestDto createAnimalRequestDto(String name, String type, String sex) {
        return new AnimalRequestDto(
                name,
                LocalDate.of(2025, 10, 10),
                1200,
                type,
                sex,
                "pequeno",
                "Animal docil"
        );
    }

    Animal createAnimal(String name, AnimalType type, AnimalSex sex, AdoptionStatus status) {
        return Animal.builder()
                .name(name)
                .birthDate(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(type)
                .sex(sex)
                .size(AnimalSize.PEQUENO)
                .registrationDate(LocalDateTime.of(2025, 10, 10, 12, 00, 00))
                .status(status)
                .description("Animal docil")
                .build();
    }
}