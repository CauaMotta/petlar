package br.com.ocauamotta.PetLar.controllers.Animal;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.dtos.User.UserSummaryDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.services.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class AnimalControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AnimalService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deve salvar um novo animal com sucesso.")
    void testSave_ShouldSaveANewAnimal() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(createAnimalRequestDto())
        );

        when(service.save(
                any(AnimalRequestDto.class),
                isNull(),
                any()
        )).thenReturn(createAnimalResponseDto(
                "1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL
        ));

        mvc.perform(multipart("/api/animals")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Rex"));

        verify(service)
                .save(any(AnimalRequestDto.class), isNull(), any());
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
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.path", is("/api/animals")));

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("Deve atualizar um animal com sucesso.")
    void testUpdate_ShouldUpdateAnAnimal() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(createAnimalRequestDto())
        );

        when(service.update(eq("123"), any(AnimalRequestDto.class), isNull(), any())).thenReturn(
                createAnimalResponseDto("123", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));

        mvc.perform(multipart("/api/animals/123")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("123")))
                .andExpect(jsonPath("$.name", is("Rex")));

        verify(service).update(eq("123"), any(AnimalRequestDto.class), isNull(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao ATUALIZAR um animal sem os campos obrigatórios.")
    void testUpdate_ShouldThrowValidationException() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes("{}")
        );

        mvc.perform(multipart("/api/animals/123")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.path", is("/api/animals/123")));

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("Deve lançar exceção ao ATUALIZAR um animal com ID inexistente.")
    void testUpdate_ShouldThrowEntityNotFoundException() throws Exception {
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(createAnimalRequestDto())
        );

        when(service.update(eq("123"), any(AnimalRequestDto.class), isNull(), any())).thenThrow(EntityNotFoundException.class);

        mvc.perform(multipart("/api/animals/123")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.path", is("/api/animals/123")));

        verify(service).update(eq("123"), any(AnimalRequestDto.class), isNull(), any());
    }

    @Test
    @DisplayName("Deve deletar um animal com sucesso.")
    void testDelete_ShouldDeleteAnAnimal() throws Exception {
        mvc.perform(delete("/api/animals/123"))
                .andExpect(status().isNoContent());

        verify(service).delete(eq("123"), any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao DELETAR um animal com ID inexistente.")
    void testDelete_ShouldThrowEntityNotFoundException() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).delete(eq("123"), any());

        mvc.perform(delete("/api/animals/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.path", is("/api/animals/123")));

        verify(service).delete(eq("123"), any());
    }

    @Test
    @DisplayName("Deve buscar um animal por ID com sucesso.")
    void testFindById_ShouldReturnAnAnimalById() throws Exception {
        when(service.findById("123")).thenReturn(
                createAnimalResponseDto("123", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL));

        mvc.perform(get("/api/animals/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("123")))
                .andExpect(jsonPath("$.name", is("Rex")));

        verify(service).findById("123");
    }

    @Test
    @DisplayName("Deve lançar exceção ao BUSCAR um animal com ID inexistente.")
    void testFindById_ShouldThrowEntityNotFoundException() throws Exception {
        doThrow(EntityNotFoundException.class).when(service).findById("123");

        mvc.perform(get("/api/animals/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.path", is("/api/animals/123")));

        verify(service).findById("123");
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com sucesso.")
    void testFindAll_ShouldReturnAPageOfAnimals() throws Exception {
        when(service.findAll(any(Pageable.class), eq("disponivel"), isNull()))
                .thenReturn(
                        new PageImpl<>(
                                List.of(
                                        createAnimalResponseDto("123", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO, AdoptionStatus.DISPONIVEL)),
                                PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is("123")))
                .andExpect(jsonPath("$.content[0].name", is("Rex")));

        verify(service).findAll(any(Pageable.class), eq("disponivel"), isNull());
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com filtros.")
    void testFindAll_ShouldReturnAPageOfAnimalsWithFilters() throws Exception {
        when(service.findAll(any(Pageable.class), eq("adotado"), eq("gato")))
                .thenReturn(
                        new PageImpl<>(
                                List.of(
                                        createAnimalResponseDto("123", "Lua", AnimalType.GATO, AnimalSex.FEMEA, AdoptionStatus.ADOTADO)),
                                PageRequest.of(0, 10), 1));

        mvc.perform(get("/api/animals?status=adotado&type=gato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is("123")))
                .andExpect(jsonPath("$.content[0].name", is("Lua")));

        verify(service).findAll(any(Pageable.class), eq("adotado"), eq("gato"));
    }

    AnimalRequestDto createAnimalRequestDto() {
        return new AnimalRequestDto(
                "Rex",
                LocalDate.of(2025, 10, 10),
                1200,
                "Cachorro",
                "Macho",
                "pequeno",
                "Animal docil"
        );
    }

    AnimalResponseDto createAnimalResponseDto(String id, String name, AnimalType type, AnimalSex sex, AdoptionStatus status) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return new AnimalResponseDto(
                id,
                name,
                LocalDate.of(2025, 10, 10),
                1200,
                type,
                sex,
                AnimalSize.PEQUENO,
                status,
                new UserSummaryDto("1", "Teste"),
                null,
                "Animal docil",
                time,
                time
        );
    }
}