package br.com.ocauamotta.PetLar.controllers.Animal;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "teste@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class AnimalControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IAnimalRepository repository;

    @Autowired
    private IUserRepository userRepository;

    private User authenticatedUser;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        userRepository.deleteAll();

        authenticatedUser = userRepository.save(
                User.builder()
                        .email("teste@teste.com")
                        .password("password")
                        .name("Teste")
                        .build()
        );
    }

    @Test
    @DisplayName("Deve salvar um animal no banco de dados com sucesso.")
    void testSave_ShouldSaveANewAnimal() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(createAnimalRequestDto("Rex", "Cachorro", "Macho"))
        );

        mvc.perform(multipart("/api/animals")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Rex")));

        List<Animal> animals = repository.findAll();
        assertTrue(animals.stream().anyMatch(a -> a.getName().equals("Rex")));
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao SALVAR um animal sem os campos obrigatórios.")
    void testSave_ShouldThrowValidationException() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes("{}")
        );

        mvc.perform(multipart("/api/animals")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar um animal no banco de dados com sucesso.")
    void testUpdate_ShouldUpdateAnAnimal() throws Exception {
        Animal savedAnimal =
                repository.insert(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL)
                );

        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(createAnimalRequestDto("Lua", "Gato", "Femea"))
        );

        mvc.perform(multipart("/api/animals/{id}", savedAnimal.getId())
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedAnimal.getId())))
                .andExpect(jsonPath("$.name", is("Lua")));

        Optional<Animal> updatedAnimal = repository.findById(savedAnimal.getId());
        assertTrue(updatedAnimal.isPresent());
        assertEquals("Lua", updatedAnimal.get().getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao ATUALIZAR um animal com ID inexistente.")
    void testUpdate_ShouldThrowEntityNotFoundException() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(createAnimalRequestDto("Lua", "Gato", "Femea"))
        );

        mvc.perform(multipart("/api/animals/123")
                .file(data)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("Deve lançar exceção ao ATUALIZAR um animal sem os campos obrigatórios.")
    void testUpdate_ShouldThrowValidationException() throws Exception {
        Animal savedAnimal =
                repository.insert(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL)
                );

        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes("{}")
        );

        mvc.perform(multipart("/api/animals/{id}", savedAnimal.getId())
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));

        Optional<Animal> updatedAnimal = repository.findById(savedAnimal.getId());
        assertTrue(updatedAnimal.isPresent());
        assertEquals("Rex", updatedAnimal.get().getName());
    }

    @Test
    @DisplayName("Deve deletar um animal do banco de dados com sucesso.")
    void testDelete_ShouldDeleteAnAnimal() throws Exception {
        Animal savedAnimal =
                repository.insert(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL)
                );

        mvc.perform(delete("/api/animals/{id}", savedAnimal.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(savedAnimal.getId()));
    }

    @Test
    @DisplayName("Deve buscar um animal por ID com sucesso.")
    void testFindById_ShouldReturnAnAnimalById() throws Exception {
        Animal savedAnimal =
                repository.insert(
                        createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL)
                );

        mvc.perform(get("/api/animals/{id}", savedAnimal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedAnimal.getId())))
                .andExpect(jsonPath("$.name", is("Rex")));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com filtros no fluxo completo.")
    void testFindAll_ShouldReturnAPageOfAnimalsWithFilters() throws Exception {
        repository.insert(createAnimal("Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));
        repository.insert(createAnimal("Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO));
        repository.insert(createAnimal("Toby", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.ADOTADO));

        // CENARIO 1: Filtro padrão
        mvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Rex")));

        // CENARIO 2: Filtro por status ADOTADO e type CACHORRO
        mvc.perform(get("/api/animals?status=adotado&type=cachorro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Toby")));

        // CENARIO 3: Filtro por status ADOTADO e type GATO
        mvc.perform(get("/api/animals?status=adotado&type=gato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Lua")));
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
                .authorId(authenticatedUser.getId())
                .description("Animal docil")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}