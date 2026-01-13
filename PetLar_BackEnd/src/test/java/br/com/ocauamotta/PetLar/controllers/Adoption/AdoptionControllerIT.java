package br.com.ocauamotta.PetLar.controllers.Adoption;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.EditReasonDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAdoptionRepository;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdoptionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IAdoptionRepository adoptionRepository;

    @Autowired
    private IAnimalRepository animalRepository;

    @Autowired
    private IUserRepository userRepository;

    private User owner;
    private User adopter;
    private Animal animal;

    @BeforeEach
    void setUp() {
        adoptionRepository.deleteAll();
        animalRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(createUser("owner@teste.com"));
        adopter = userRepository.save(createUser("adopter@teste.com"));
        animal = animalRepository.save(createAnimal(owner.getId()));
    }

    @Test
    @DisplayName("Deve solicitar adoção.")
    @WithUserDetails(value = "adopter@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testInitAdoption_Integration() throws Exception {
        mockMvc.perform(post("/api/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AdoptionRequestDto(animal.getId(), "Quero muito adotar"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.reason").value("Quero muito adotar"))
                .andExpect(jsonPath("$.adopter.id").value(adopter.getId()));
    }

    @Test
    @DisplayName("Deve listar solicitações do usuário.")
    @WithUserDetails(value = "adopter@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAdoptionsRequestedByMe_Integration() throws Exception {
        createAdoptionInDb();

        mockMvc.perform(get("/api/adoptions/me/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animal.id").value(animal.getId()));
    }

    @Test
    @DisplayName("Deve listar solicitações para os animais do dono.")
    @WithUserDetails(value = "owner@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetRequestsForMyAnimals_Integration() throws Exception {
        createAdoptionInDb();

        mockMvc.perform(get("/api/adoptions/me/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].animalOwner.id").value(owner.getId()));
    }

    @Test
    @DisplayName("Deve cancelar adoção.")
    @WithUserDetails(value = "adopter@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testCancelAdoption_Integration() throws Exception {
        Adoption adoption = createAdoptionInDb();

        mockMvc.perform(patch("/api/adoptions/{id}/cancel", adoption.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    @DisplayName("Deve aprovar adoção.")
    @WithUserDetails(value = "owner@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAcceptAdoption_Integration() throws Exception {
        Adoption adoption = createAdoptionInDb();

        mockMvc.perform(patch("/api/adoptions/{id}/accept", adoption.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APROVADO"));
    }

    @Test
    @DisplayName("Deve recusar adoção.")
    @WithUserDetails(value = "owner@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDenyAdoption_Integration() throws Exception {
        Adoption adoption = createAdoptionInDb();

        mockMvc.perform(patch("/api/adoptions/{id}/deny", adoption.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECUSADO"));
    }

    @Test
    @DisplayName("Deve editar a justificativa.")
    @WithUserDetails(value = "adopter@teste.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditReason_Integration() throws Exception {
        Adoption adoption = createAdoptionInDb();

        mockMvc.perform(patch("/api/adoptions/{id}", adoption.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EditReasonDto("Novo motivo alterado"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Novo motivo alterado"));
    }

    private Adoption createAdoptionInDb() {
        return adoptionRepository.insert(
                Adoption.builder()
                        .animalId(animal.getId())
                        .adopterId(adopter.getId())
                        .animalOwnerId(owner.getId())
                        .status(AdoptionStatus.PENDENTE)
                        .reason("Teste inicial")
                        .createdAt(ZonedDateTime.now().toString())
                        .updatedAt(ZonedDateTime.now().toString())
                        .build()
        );
    }

    private User createUser(String email) {
        String time =
                ZonedDateTime.of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                        .toString();

        return User.builder()
                .email(email)
                .password("secretPassword")
                .name("Teste")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }

    private Animal createAnimal(String authorId) {
        String time =
                ZonedDateTime.of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                        .toString();

        return Animal.builder()
                .name("Rex")
                .birthDate(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(AnimalType.CACHORRO)
                .sex(AnimalSex.MACHO)
                .size(AnimalSize.PEQUENO)
                .status(AdoptionStatus.DISPONIVEL)
                .authorId(authorId)
                .description("Animal docil")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}