package br.com.ocauamotta.PetLar.integration.controller;

import br.com.ocauamotta.PetLar.domain.Cat;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.CatMapper;
import br.com.ocauamotta.PetLar.repository.ICatRepository;
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
class CatControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICatRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSave_ShouldPersistAndReturnCat() throws Exception {
        CreateAnimalDTO createAnimalDTO = createAnimalDTO();

        mockMvc.perform(post("/cats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAnimalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mimi"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedCat() throws Exception {
        Cat saved = repository.save(cat());

        mockMvc.perform(get("/cats/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Mimi"));

        saved.setName("Luna");
        AnimalDTO animalDTO = CatMapper.toDTO(saved);

        mockMvc.perform(put("/cats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void testUpdate_ShouldReturnBadRequest_WhenUpdatingWithoutId() throws Exception {
        Cat saved = repository.save(cat());
        saved.setName("Luna");
        saved.setId(null);
        AnimalDTO animalDTO = CatMapper.toDTO(saved);

        mockMvc.perform(put("/cats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindById_ShouldReturnCat() throws Exception {
        Cat saved = repository.save(cat());

        mockMvc.perform(get("/cats/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Mimi"));
    }

    @Test
    void testFindById_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/cats/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ShouldRemoveCat() throws Exception {
        Cat saved = repository.save(cat());

        mockMvc.perform(delete("/cats/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Removido com sucesso.")));

        assertTrue(repository.findById(saved.getId()).isEmpty());
    }

    @Test
    void testFindAll_ShouldReturnPagedCats() throws Exception {
        repository.save(cat());
        repository.save(cat("Luna"));

        mockMvc.perform(get("/cats")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Mimi"))
                .andExpect(jsonPath("$.content[1].name").value("Luna"));
    }

    @Test
    void testFindAll_ShouldReturnPagedCats_WithStatusAvailable() throws Exception {
        repository.save(cat());
        repository.save(cat("Luna"));

        mockMvc.perform(get("/cats?status=Disponível")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Mimi"))
                .andExpect(jsonPath("$.content[0].status").value("Disponível"))
                .andExpect(jsonPath("$.content[1].name").value("Luna"))
                .andExpect(jsonPath("$.content[1].status").value("Disponível"));
    }

    @Test
    void testFindAll_ShouldReturnPagedCats_WithStatusAdopted() throws Exception {
        Cat dog1 = cat();
        dog1.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog1);

        Cat dog2 = cat("Luna");
        dog2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog2);

        mockMvc.perform(get("/cats?status=Adotado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Mimi"))
                .andExpect(jsonPath("$.content[0].status").value("Adotado"))
                .andExpect(jsonPath("$.content[1].name").value("Luna"))
                .andExpect(jsonPath("$.content[1].status").value("Adotado"));
    }

    Cat cat() {
        return Cat.builder()
                .name("Mimi")
                .age(12)
                .type(AnimalType.CAT)
                .breed("SRD")
                .sex(AnimalSex.FEMALE)
                .weight(400)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Carinhosa.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    Cat cat(String name) {
        return Cat.builder()
                .name(name)
                .age(12)
                .type(AnimalType.CAT)
                .breed("SRD")
                .sex(AnimalSex.FEMALE)
                .weight(400)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Carinhosa.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
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
