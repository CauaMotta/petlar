package br.com.ocauamotta.PetLar.integration.controller;

import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.OtherMapper;
import br.com.ocauamotta.PetLar.repository.IOtherRepository;
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
class OtherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IOtherRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSave_ShouldPersistAndReturnOther() throws Exception {
        CreateAnimalDTO createAnimalDTO = createAnimalDTO();

        mockMvc.perform(post("/api/others")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAnimalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bolt"))
                .andExpect(jsonPath("$.age").value(48));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedOther() throws Exception {
        Other saved = repository.save(other());

        mockMvc.perform(get("/api/others/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Bolt"));

        saved.setName("Pernalonga");
        AnimalDTO animalDTO = OtherMapper.toDTO(saved);

        mockMvc.perform(put("/api/others")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pernalonga"))
                .andExpect(jsonPath("$.age").value(48));
    }

    @Test
    void testUpdate_ShouldReturnBadRequest_WhenUpdatingWithoutId() throws Exception {
        Other saved = repository.save(other());
        saved.setName("Pernalonga");
        saved.setId(null);
        AnimalDTO animalDTO = OtherMapper.toDTO(saved);

        mockMvc.perform(put("/api/others")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindById_ShouldReturnOther() throws Exception {
        Other saved = repository.save(other());

        mockMvc.perform(get("/api/others/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Bolt"));
    }

    @Test
    void testFindById_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/others/{id}", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ShouldRemoveOther() throws Exception {
        Other saved = repository.save(other());

        mockMvc.perform(delete("/api/others/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Removido com sucesso.")));

        assertTrue(repository.findById(saved.getId()).isEmpty());
    }

    @Test
    void testFindAll_ShouldReturnPagedOthers() throws Exception {
        repository.save(other());
        repository.save(other("Pernalonga"));

        mockMvc.perform(get("/api/others")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Bolt"))
                .andExpect(jsonPath("$.content[1].name").value("Pernalonga"));
    }

    @Test
    void testFindAll_ShouldReturnPagedOthers_WithStatusAvailable() throws Exception {
        repository.save(other());
        repository.save(other("Pernalonga"));

        mockMvc.perform(get("/api/others?status=Disponível")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Bolt"))
                .andExpect(jsonPath("$.content[0].status").value("Disponível"))
                .andExpect(jsonPath("$.content[1].name").value("Pernalonga"))
                .andExpect(jsonPath("$.content[1].status").value("Disponível"));
    }

    @Test
    void testFindAll_ShouldReturnPagedOthers_WithStatusAdopted() throws Exception {
        Other dog1 = other();
        dog1.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog1);

        Other dog2 = other("Pernalonga");
        dog2.setStatus(AdoptionStatus.ADOPTED);
        repository.save(dog2);

        mockMvc.perform(get("/api/others?status=Adotado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Bolt"))
                .andExpect(jsonPath("$.content[0].status").value("Adotado"))
                .andExpect(jsonPath("$.content[1].name").value("Pernalonga"))
                .andExpect(jsonPath("$.content[1].status").value("Adotado"));
    }

    Other other() {
        return Other.builder()
                .name("Bolt")
                .age(48)
                .type(AnimalType.OTHER)
                .breed("Coelho")
                .sex(AnimalSex.MALE)
                .weight(250)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Animal dócil e brincalhão.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    Other other(String name) {
        return Other.builder()
                .name(name)
                .age(48)
                .type(AnimalType.OTHER)
                .breed("Coelho")
                .sex(AnimalSex.MALE)
                .weight(250)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Animal dócil e brincalhão.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
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
