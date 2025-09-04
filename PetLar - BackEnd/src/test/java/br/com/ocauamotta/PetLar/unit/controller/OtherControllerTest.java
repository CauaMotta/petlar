package br.com.ocauamotta.PetLar.unit.controller;

import br.com.ocauamotta.PetLar.controller.BirdController;
import br.com.ocauamotta.PetLar.controller.OtherController;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.service.BirdService;
import br.com.ocauamotta.PetLar.service.OtherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OtherController.class)
class OtherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OtherService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testSave_ShouldReturnCreatedOther() throws Exception {
        CreateAnimalDTO createDto = createAnimalDTO();
        AnimalDTO returnedDto = animalDTO();
        when(service.save(Mockito.any(CreateAnimalDTO.class))).thenReturn(returnedDto);

        mockMvc.perform(post("/others")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Bolt")));

        verify(service, times(1)).save(Mockito.any(CreateAnimalDTO.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedOther() throws Exception {
        AnimalDTO animalDTO = animalDTO();
        when(service.update(Mockito.any(AnimalDTO.class))).thenReturn(animalDTO);

        mockMvc.perform(put("/others")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bolt")));

        verify(service, times(1)).update(Mockito.any(AnimalDTO.class));
    }

    @Test
    void testUpdate_ShouldReturnBadRequest_WhenUpdatingWithoutId() throws Exception {
        AnimalDTO animalDTO = animalDTO();
        animalDTO.setId(null);

        mockMvc.perform(put("/others")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(animalDTO)))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).update(Mockito.any(AnimalDTO.class));
    }

    @Test
    void testDelete_ShouldReturnNoContent_WhenIdExists() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/others/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Removido com sucesso."));

        verify(service, times(1)).delete("1");
    }

    @Test
    void testFindById_ShouldReturnOther_WhenExists() throws Exception {
        AnimalDTO animalDTO = animalDTO();

        when(service.findById("1")).thenReturn(animalDTO);

        mockMvc.perform(get("/others/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Bolt")));
    }

    @Test
    void testFindById_ShouldReturnNotFound_WhenOtherDoesNotExist() throws Exception {
        when(service.findById("1")).thenThrow(new EntityNotFoundException("Animal not found with ID: 1"));

        mockMvc.perform(get("/others/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAll_ShouldReturnPagedOthers() throws Exception {
        AnimalDTO animalDTO = animalDTO();
        when(service.findAll(Mockito.any(Pageable.class), Mockito.isNull()))
                .thenReturn(new PageImpl<>(List.of(animalDTO), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/others")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Bolt")));
    }

    @Test
    void testFindAll_ShouldReturnPagedOthers_WithStatusAvailable() throws Exception {
        AnimalDTO animalDTO = animalDTO();
        when(service.findAll(Mockito.any(Pageable.class), Mockito.eq("Disponível")))
                .thenReturn(new PageImpl<>(List.of(animalDTO), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/others?status=Disponível")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Bolt")))
                .andExpect(jsonPath("$.content[0].status", is("Disponível")));
    }

    @Test
    void testFindAll_ShouldReturnPagedOthers_WithStatusAdopted() throws Exception {
        AnimalDTO animalDTO = animalDTO();
        animalDTO.setStatus(AdoptionStatus.ADOPTED.getLabel());
        when(service.findAll(Mockito.any(Pageable.class), Mockito.eq("Adotado")))
                .thenReturn(new PageImpl<>(List.of(animalDTO), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/others?status=Adotado")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Bolt")))
                .andExpect(jsonPath("$.content[0].status", is("Adotado")));
    }

    AnimalDTO animalDTO() {
        return AnimalDTO.builder()
                .id("1")
                .name("Bolt")
                .age(48)
                .type(AnimalType.OTHER.getLabel())
                .breed("Coelho")
                .sex(AnimalSex.MALE.getLabel())
                .weight(250)
                .size(AnimalSize.MEDIUM.getLabel())
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE.getLabel())
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
