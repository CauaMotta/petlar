package br.com.ocauamotta.PetLar.unit.controller;

import br.com.ocauamotta.PetLar.controller.DogController;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.service.DogService;
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

@WebMvcTest(DogController.class)
public class DogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DogService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testSave_ShouldReturnCreatedDog() throws Exception {
        CreateDogDTO createDto = createDogDTO();
        DogDTO returnedDto = dogDTO();
        when(service.save(Mockito.any(CreateDogDTO.class))).thenReturn(returnedDto);

        mockMvc.perform(post("/dog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Rex")));

        verify(service, times(1)).save(Mockito.any(CreateDogDTO.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedDog() throws Exception {
        DogDTO dogDTO = dogDTO();
        when(service.update(Mockito.any(DogDTO.class))).thenReturn(dogDTO);

        mockMvc.perform(put("/dog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dogDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Rex")));

        verify(service, times(1)).update(Mockito.any(DogDTO.class));
    }

    @Test
    void testDelete_ShouldReturnNoContent_WhenIdExists() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/dog/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Removido com sucesso."));

        verify(service, times(1)).delete("1");
    }

    @Test
    void testFindById_ShouldReturnDog_WhenExists() throws Exception {
        DogDTO dogDTO = dogDTO();

        when(service.findById("1")).thenReturn(dogDTO);

        mockMvc.perform(get("/dog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("Rex")));
    }

    @Test
    void testFindById_ShouldReturnNotFound_WhenDogDoesNotExist() throws Exception {
        when(service.findById("1")).thenThrow(new EntityNotFoundException("Dog not found with ID: 1"));

        mockMvc.perform(get("/dog/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAll_ShouldReturnPagedDogs() throws Exception {
        DogDTO dogDTO = dogDTO();
        when(service.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dogDTO), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/dog")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Rex")));
    }

    DogDTO dogDTO() {
        return DogDTO.builder()
                .id("1")
                .name("Rex")
                .yearsOld(3)
                .type(AnimalType.DOG)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .status(AdoptionStatus.AVAILABLE)
                .registrationDate(LocalDate.now())
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }

    CreateDogDTO createDogDTO() {
        return CreateDogDTO.builder()
                .name("Rex")
                .yearsOld(3)
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
