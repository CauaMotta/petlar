package br.com.ocauamotta.PetLar.services.Animal;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.services.AnimalService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnimalServiceIT {

    @Autowired
    private AnimalService service;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAnimalRepository animalRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        animalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar um novo animal e busca-lo no banco de dados com sucesso.")
    void testSaveAndFindById_ShouldSaveAndFindAnAnimal() {
        AnimalResponseDto savedAnimal =
                service.save(
                        createAnimalRequestDto("Rex", "Cachorro", "Macho"),
                        null,
                        createUser()
                );

        assertNotNull(savedAnimal.id());

        AnimalResponseDto foundAnimal = service.findById(savedAnimal.id());

        assertEquals(savedAnimal.name(), foundAnimal.name());
        assertEquals(savedAnimal.status(), foundAnimal.status());
    }

    @Test
    @DisplayName("Deve atualizar um animal com sucesso.")
    void testUpdate_ShouldUpdateAnAnimal() {
        User user = createUser();
        AnimalResponseDto savedAnimal =
                service.save(
                        createAnimalRequestDto("Rex", "Cachorro", "Macho"),
                        null,
                        user
                );

        AnimalResponseDto updatedAnimal =
                service.update(
                        savedAnimal.id(), createAnimalRequestDto("Lua", "Gato", "Femea"),
                        null,
                        user
                );

        assertNotNull(updatedAnimal);
        assertEquals(savedAnimal.id(), updatedAnimal.id());
        assertEquals("Lua", updatedAnimal.name());
        assertEquals(AnimalType.GATO, updatedAnimal.type());
    }

    @Test
    @DisplayName("Deve deletar um animal com sucesso")
    void testDelete_ShouldDeleteAnAnimal() {
        User user = createUser();
        AnimalResponseDto savedAnimal =
                service.save(
                        createAnimalRequestDto("Rex", "Cachorro", "Macho"),
                        null,
                        user
                );

        service.delete(savedAnimal.id(), user);

        assertThrows(EntityNotFoundException.class, () -> service.findById(savedAnimal.id()));
    }

    @Test
    @DisplayName("Deve buscar todos os animais sem filtros.")
    void testFindAll_ShouldReturnAllAnimals() {
        User user = createUser();
        animalRepository.saveAll(
                List.of(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Bob", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Miau", AnimalType.GATO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user)
                ));

        Page<AnimalResponseDto> page = service.findAll(PageRequest.of(0, 10), "disponivel", null);

        assertEquals(4, page.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar todos os animais com filtro de espécie.")
    void testFindAll_ShouldReturnAllAnimalsWithTypeFilter() {
        User user = createUser();
        animalRepository.saveAll(
                List.of(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Bob", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Miau", AnimalType.GATO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user)
                ));

        Page<AnimalResponseDto> page = service.findAll(PageRequest.of(0, 10), "disponivel", "gato");

        assertEquals(2, page.getTotalElements());
        assertEquals(AnimalType.GATO, page.getContent().getFirst().type());
    }

    @Test
    @DisplayName("Deve buscar todos os animais com filtro de status da adoção.")
    void testFindAll_ShouldReturnAllAnimalsWithStatusFilter() {
        User user = createUser();
        animalRepository.saveAll(
                List.of(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.ADOTADO, user),
                        createAnimal("Bob", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user),
                        createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO, user),
                        createAnimal("Miau", AnimalType.GATO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL, user)
                ));

        Page<AnimalResponseDto> page = service.findAll(PageRequest.of(0, 10), "adotado", null);

        assertEquals(2, page.getTotalElements());
        assertEquals(AdoptionStatus.ADOTADO, page.getContent().getFirst().status());
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

    User createUser() {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return userRepository.insert(
                User.builder()
                        .email("user@teste.com")
                        .password("secretPassword")
                        .name("Teste")
                        .createdAt(time)
                        .updatedAt(time)
                        .build());
    }

    Animal createAnimal(String name, AnimalType type, AnimalSex sex, AdoptionStatus status, User user) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return Animal.builder()
                .name(name)
                .birthDate(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(type)
                .sex(sex)
                .size(AnimalSize.PEQUENO)
                .status(status)
                .authorId(user.getId())
                .description("Animal docil")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}