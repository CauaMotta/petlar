package br.com.ocauamotta.PetLar.services.Adoption;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
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
import br.com.ocauamotta.PetLar.services.AdoptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdoptionServiceIT {

    @Autowired
    private AdoptionService service;

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
        owner = userRepository.save(createUser("owner@teste.com"));
        adopter = userRepository.save(createUser("adopter@teste.com"));
        animal = animalRepository.save(createAnimal(owner.getId()));
    }

    @AfterEach
    void tearDown() {
        adoptionRepository.deleteAll();
        animalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve iniciar uma adoção com sucesso e atualizar status do animal.")
    void testInitAdoption_shouldCreateAdoptionAndUpdateAnimalStatus() {
        AdoptionRequestDto dto = new AdoptionRequestDto(
                animal.getId(),
                "Quero adotar porque amo animais."
        );

        AdoptionResponseDto response = service.initAdoption(dto, adopter);

        assertNotNull(response);
        assertEquals(AdoptionStatus.PENDENTE, response.status());

        Adoption adoptionSaved = adoptionRepository.findById(response.id()).orElseThrow();
        assertEquals(adopter.getId(), adoptionSaved.getAdopterId());
        assertEquals(owner.getId(), adoptionSaved.getAnimalOwnerId());

        Animal updatedAnimal = animalRepository.findById(animal.getId()).orElseThrow();
        assertEquals(AdoptionStatus.PENDENTE, updatedAnimal.getStatus());
    }

    @Test
    @DisplayName("Deve cancelar uma adoção pendente e liberar o animal.")
    void testCancelAdoption_shouldCancelAdoptionAndReleaseAnimal() {
        Adoption adoption = createAdoption();

        AdoptionResponseDto response = service.cancelAdoption(adoption.getId(), adopter);

        assertEquals(AdoptionStatus.CANCELADO, response.status());

        Adoption updatedAdoption = adoptionRepository.findById(adoption.getId()).orElseThrow();
        assertEquals(AdoptionStatus.CANCELADO, updatedAdoption.getStatus());

        Animal updatedAnimal = animalRepository.findById(animal.getId()).orElseThrow();
        assertEquals(AdoptionStatus.DISPONIVEL, updatedAnimal.getStatus());
    }

    @Test
    @DisplayName("Deve aprovar uma adoção pendente e marcar o animal como adotado.")
    void testAcceptAdoption_shouldApproveAdoptionAndAdoptAnimal() {
        Adoption adoption = createAdoption();

        AdoptionResponseDto response = service.acceptAdoption(adoption.getId(), owner);

        assertEquals(AdoptionStatus.APROVADO, response.status());

        Adoption updatedAdoption = adoptionRepository.findById(adoption.getId()).orElseThrow();
        assertEquals(AdoptionStatus.APROVADO, updatedAdoption.getStatus());

        Animal updatedAnimal = animalRepository.findById(animal.getId()).orElseThrow();
        assertEquals(AdoptionStatus.ADOTADO, updatedAnimal.getStatus());
    }

    @Test
    @DisplayName("Deve recusar uma adoção pendente e liberar o animal.")
    void testDenyAdoption_shouldDenyAdoptionAndReleaseAnimal() {
        Adoption adoption = createAdoption();

        AdoptionResponseDto response = service.denyAdoption(adoption.getId(), owner);

        assertEquals(AdoptionStatus.RECUSADO, response.status());

        Adoption updatedAdoption = adoptionRepository.findById(adoption.getId()).orElseThrow();
        assertEquals(AdoptionStatus.RECUSADO, updatedAdoption.getStatus());

        Animal updatedAnimal = animalRepository.findById(animal.getId()).orElseThrow();
        assertEquals(AdoptionStatus.DISPONIVEL, updatedAnimal.getStatus());
    }

    @Test
    @DisplayName("Deve editar a justificativa de uma adoção pendente")
    void testEditReason_shouldUpdateAdoptionReason() {
        Adoption adoption = createAdoption();

        AdoptionResponseDto response = service.editReason(
                adoption.getId(),
                new EditReasonDto("Novo motivo"),
                adopter
        );

        assertEquals("Novo motivo", response.reason());

        Adoption updatedAdoption = adoptionRepository.findById(adoption.getId()).orElseThrow();
        assertEquals("Novo motivo", updatedAdoption.getReason());
    }

    @Test
    @DisplayName("Deve retornar adoções solicitadas pelo usuário autenticado.")
    void testGetAdoptionsRequestedByMe_shouldReturnUserAdoptions() {
        createAdoption();

        Page<AdoptionResponseDto> page = service.getAdoptionsRequestedByMe(
                PageRequest.of(0, 10),
                adopter
        );

        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar solicitações de adoção para os animais do usuário")
    void testGetRequestsForMyAnimals_shouldReturnRequestsForOwnerAnimals() {
        createAdoption();

        Page<AdoptionResponseDto> page = service.getRequestsForMyAnimals(
                PageRequest.of(0, 10),
                owner
        );

        assertEquals(1, page.getTotalElements());
    }

    Adoption createAdoption() {
        return adoptionRepository.insert(
                Adoption.builder()
                        .animalId(animal.getId())
                        .adopterId(adopter.getId())
                        .animalOwnerId(owner.getId())
                        .status(AdoptionStatus.PENDENTE)
                        .reason("Teste")
                        .build()
        );
    }

    User createUser(String email) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return User.builder()
                .email(email)
                .password("secretPassword")
                .name("Teste")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }

    Animal createAnimal(String authorId) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
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