package br.com.ocauamotta.PetLar.integration.controller;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.DogMapper;
import br.com.ocauamotta.PetLar.repository.IDogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DogControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IDogRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSave_ShouldPersistAndReturnDog() throws Exception {
        CreateDogDTO createDogDTO = createDogDTO();

        mockMvc.perform(post("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDogDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedDog() throws Exception {
        Dog saved = repository.save(createDog());

        mockMvc.perform(get("/api/dogs/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Rex"));

        saved.setName("Bob");
        DogDTO dogDTO = DogMapper.toDTO(saved);

        mockMvc.perform(put("/api/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dogDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    void testFindById_ShouldReturnDog() throws Exception {
        Dog saved = repository.save(createDog());

        mockMvc.perform(get("/api/dogs/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    @Test
    void testFindById_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/dogs/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ShouldRemoveDog() throws Exception {
        Dog saved = repository.save(createDog());

        mockMvc.perform(delete("/api/dogs/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Removido com sucesso.")));

        assertTrue(repository.findById(saved.getId()).isEmpty());
    }

    @Test
    void testFindAll_ShouldReturnPagedDogs() throws Exception {
        repository.save(createDog());
        repository.save(createDog("Bob"));

        mockMvc.perform(get("/api/dogs")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Rex"))
                .andExpect(jsonPath("$.content[1].name").value("Bob"));
    }

    @Test
    void testFindAll_ShouldReturnPagedDogs_WithStatusAvailable() throws Exception {
        repository.save(createDog());
        repository.save(createDog("Bob"));

        mockMvc.perform(get("/api/dogs?status=Disponível")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Rex"))
                .andExpect(jsonPath("$.content[0].status").value("Disponível"))
                .andExpect(jsonPath("$.content[1].name").value("Bob"))
                .andExpect(jsonPath("$.content[1].status").value("Disponível"));
    }

    @Test
    void testFindAll_ShouldReturnPagedDogs_WithStatusAdopted() throws Exception {
        Dog dog1 = createDog();
        dog1.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog1);

        Dog dog2 = createDog("Bob");
        dog2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog2);

        mockMvc.perform(get("/api/dogs?status=Adotado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Rex"))
                .andExpect(jsonPath("$.content[0].status").value("Adotado"))
                .andExpect(jsonPath("$.content[1].name").value("Bob"))
                .andExpect(jsonPath("$.content[1].status").value("Adotado"));
    }

    Dog createDog() {
        return Dog.builder()
                .name("Rex")
                .age(3)
                .type(AnimalType.DOG)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }

    Dog createDog(String name) {
        return Dog.builder()
                .name(name)
                .age(3)
                .type(AnimalType.DOG)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
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
