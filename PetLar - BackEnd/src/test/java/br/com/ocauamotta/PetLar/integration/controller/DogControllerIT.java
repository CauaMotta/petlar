package br.com.ocauamotta.PetLar.integration.controller;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
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

        mockMvc.perform(post("/dog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDogDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedDog() throws Exception {
        Dog saved = repository.save(createDog());

        mockMvc.perform(get("/dog/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Rex"));

        saved.setName("Bob");

        mockMvc.perform(put("/dog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    void testFindById_ShouldReturnDog() throws Exception {
        Dog saved = repository.save(createDog());

        mockMvc.perform(get("/dog/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    @Test
    void testFindById_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/dog/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ShouldRemoveDog() throws Exception {
        Dog saved = repository.save(createDog());

        mockMvc.perform(delete("/dog/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Removido com sucesso.")));

        assertTrue(repository.findById(saved.getId()).isEmpty());
    }

    @Test
    void testFindAll_ShouldReturnPagedDogs() throws Exception {
        repository.save(createDog());
        repository.save(createDog("Bob"));

        mockMvc.perform(get("/dog")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Rex"))
                .andExpect(jsonPath("$.content[1].name").value("Bob"));
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
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }
}
