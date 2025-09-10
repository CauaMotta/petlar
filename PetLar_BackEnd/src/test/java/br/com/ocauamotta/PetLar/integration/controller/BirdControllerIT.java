package br.com.ocauamotta.PetLar.integration.controller;

import br.com.ocauamotta.PetLar.domain.Bird;
import br.com.ocauamotta.PetLar.domain.Bird;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.BirdMapper;
import br.com.ocauamotta.PetLar.repository.IBirdRepository;
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
class BirdControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IBirdRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSave_ShouldPersistAndReturnBird() throws Exception {
        CreateAnimalDTO createAnimalDTO = createAnimalDTO();

        mockMvc.perform(post("/api/birds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAnimalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Loro"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedBird() throws Exception {
        Bird saved = repository.save(bird());

        mockMvc.perform(get("/api/birds/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Loro"));

        saved.setName("José");
        AnimalDTO animalDTO = BirdMapper.toDTO(saved);

        mockMvc.perform(put("/api/birds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("José"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void testUpdate_ShouldReturnBadRequest_WhenUpdatingWithoutId() throws Exception {
        Bird saved = repository.save(bird());
        saved.setName("José");
        saved.setId(null);
        AnimalDTO animalDTO = BirdMapper.toDTO(saved);

        mockMvc.perform(put("/api/birds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindById_ShouldReturnBird() throws Exception {
        Bird saved = repository.save(bird());

        mockMvc.perform(get("/api/birds/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Loro"));
    }

    @Test
    void testFindById_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/birds/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ShouldRemoveBird() throws Exception {
        Bird saved = repository.save(bird());

        mockMvc.perform(delete("/api/birds/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Removido com sucesso.")));

        assertTrue(repository.findById(saved.getId()).isEmpty());
    }

    @Test
    void testFindAll_ShouldReturnPagedBirds() throws Exception {
        repository.save(bird());
        repository.save(bird("José"));

        mockMvc.perform(get("/api/birds")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Loro"))
                .andExpect(jsonPath("$.content[1].name").value("José"));
    }

    @Test
    void testFindAll_ShouldReturnPagedBirds_WithStatusAvailable() throws Exception {
        repository.save(bird());
        repository.save(bird("José"));

        mockMvc.perform(get("/api/birds?status=Disponível")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Loro"))
                .andExpect(jsonPath("$.content[0].status").value("Disponível"))
                .andExpect(jsonPath("$.content[1].name").value("José"))
                .andExpect(jsonPath("$.content[1].status").value("Disponível"));
    }

    @Test
    void testFindAll_ShouldReturnPagedBirds_WithStatusAdopted() throws Exception {
        Bird dog1 = bird();
        dog1.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog1);

        Bird dog2 = bird("José");
        dog2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog2);

        mockMvc.perform(get("/api/birds?status=Adotado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Loro"))
                .andExpect(jsonPath("$.content[0].status").value("Adotado"))
                .andExpect(jsonPath("$.content[1].name").value("José"))
                .andExpect(jsonPath("$.content[1].status").value("Adotado"));
    }

    Bird bird() {
        return Bird.builder()
                .name("Loro")
                .age(12)
                .type(AnimalType.BIRD)
                .breed("Papagaio")
                .sex(AnimalSex.MALE)
                .weight(50)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Pássaro alegre e falador.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    Bird bird(String name) {
        return Bird.builder()
                .name(name)
                .age(12)
                .type(AnimalType.BIRD)
                .breed("Papagaio")
                .sex(AnimalSex.MALE)
                .weight(50)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Pássaro alegre e falador.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Loro")
                .age(12)
                .breed("Papagaio")
                .sex(AnimalSex.MALE.getLabel())
                .weight(50)
                .size(AnimalSize.SMALL.getLabel())
                .description("Pássaro alegre e falador.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
