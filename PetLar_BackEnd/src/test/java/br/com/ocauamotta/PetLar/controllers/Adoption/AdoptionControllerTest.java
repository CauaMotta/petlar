package br.com.ocauamotta.PetLar.controllers.Adoption;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.EditReasonDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.services.AdoptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class AdoptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdoptionService service;

    @Test
    @DisplayName("initAdoption: Deve retornar 201 Created quando a solicitação for válida.")
    void testInitAdoption_shouldReturnCreated() throws Exception {
        when(service.initAdoption(any(AdoptionRequestDto.class), any()))
                .thenReturn(createAdoptionResponseDto(AdoptionStatus.PENDENTE));

        mockMvc.perform(post("/api/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdoptionRequestDto("animal-1", "Quero adotar"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    @DisplayName("initAdoption: Deve retornar 400 Bad Request quando o body for inválido.")
    void testInitAdoption_shouldReturnBadRequest_whenInvalid() throws Exception {
        mockMvc.perform(post("/api/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdoptionRequestDto("", ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("getAdoptionsRequestedByMe: Deve retornar 200 OK e lista paginada.")
    void testGetAdoptionsRequestedByMe_shouldReturnPage() throws Exception {
        Page<AdoptionResponseDto> page = new PageImpl<>(List.of(createAdoptionResponseDto(AdoptionStatus.PENDENTE)));

        when(service.getAdoptionsRequestedByMe(any(Pageable.class), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/adoptions/me/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("123"));
    }

    @Test
    @DisplayName("getRequestsForMyAnimals: Deve retornar 200 OK e lista paginada.")
    void testGetRequestsForMyAnimals_shouldReturnPage() throws Exception {
        Page<AdoptionResponseDto> page = new PageImpl<>(List.of(createAdoptionResponseDto(AdoptionStatus.PENDENTE)));

        when(service.getRequestsForMyAnimals(any(Pageable.class), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/adoptions/me/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("123"));
    }

    @Test
    @DisplayName("cancelAdoption: Deve retornar 200 OK e status CANCELADO.")
    void testCancelAdoption_shouldReturnOk() throws Exception {
        when(service.cancelAdoption(eq("123"), any()))
                .thenReturn(createAdoptionResponseDto(AdoptionStatus.CANCELADO));

        mockMvc.perform(patch("/api/adoptions/123/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    @DisplayName("acceptAdoption: Deve retornar 200 OK e status APROVADO.")
    void testAcceptAdoption_shouldReturnOk() throws Exception {
        when(service.acceptAdoption(eq("123"), any()))
                .thenReturn(createAdoptionResponseDto(AdoptionStatus.APROVADO));

        mockMvc.perform(patch("/api/adoptions/123/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APROVADO"));
    }

    @Test
    @DisplayName("denyAdoption: Deve retornar 200 OK e status RECUSADO.")
    void testDenyAdoption_shouldReturnOk() throws Exception {
        when(service.denyAdoption(eq("123"), any()))
                .thenReturn(createAdoptionResponseDto(AdoptionStatus.RECUSADO));

        mockMvc.perform(patch("/api/adoptions/123/deny"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECUSADO"));
    }

    @Test
    @DisplayName("editReason: Deve retornar 200 OK com justificativa atualizada.")
    void testEditReason_shouldReturnOk() throws Exception {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        AdoptionResponseDto updatedResponse = new AdoptionResponseDto(
                "123", AdoptionStatus.PENDENTE, null, null, null, "Novo motivo atualizado", time, time
        );

        when(service.editReason(eq("123"), any(EditReasonDto.class), any()))
                .thenReturn(updatedResponse);

        mockMvc.perform(patch("/api/adoptions/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EditReasonDto("Novo motivo atualizado"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Novo motivo atualizado"));
    }

    AdoptionResponseDto createAdoptionResponseDto(AdoptionStatus status) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return new AdoptionResponseDto(
                "123",
                status,
                null,
                null,
                null,
                "Motivo teste",
                time,
                time
        );
    }
}