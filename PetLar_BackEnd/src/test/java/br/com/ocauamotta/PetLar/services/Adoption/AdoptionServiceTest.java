package br.com.ocauamotta.PetLar.services.Adoption;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.EditReasonDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AdoptionAlreadyProcessedException;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AnimalNotAvailableException;
import br.com.ocauamotta.PetLar.exceptions.Adoption.UserNotOwnershipException;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.exceptions.User.UserInactiveException;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAdoptionRepository;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.services.AdoptionService;
import br.com.ocauamotta.PetLar.validations.Adoption.AdopterOwnershipValidation;
import br.com.ocauamotta.PetLar.validations.Adoption.AnimalOwnershipValidation;
import br.com.ocauamotta.PetLar.validations.Adoption.PendingAdoptionValidation;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalNotAvailableValidation;
import br.com.ocauamotta.PetLar.validations.Animal.TryAdoptionYourOwnPetValidation;
import br.com.ocauamotta.PetLar.validations.User.UserActiveYetValidation;
import br.com.ocauamotta.PetLar.exceptions.Adoption.TryAdoptionYourOwnPetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdoptionServiceTest {

    @Mock
    private IAdoptionRepository adoptionRepository;

    @Mock
    private IAnimalRepository animalRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private TryAdoptionYourOwnPetValidation tryAdoptionYourOwnPetValidation;

    @Mock
    private AnimalNotAvailableValidation animalNotAvailableValidation;

    @Mock
    private PendingAdoptionValidation pendingAdoptionValidation;

    @Mock
    private AdopterOwnershipValidation adopterOwnershipValidation;

    @Mock
    private UserActiveYetValidation userActiveYetValidation;

    @Mock
    private AnimalOwnershipValidation animalOwnershipValidation;

    @InjectMocks
    private AdoptionService service;

    @Test
    @DisplayName("initAdoption: deve iniciar uma adoção com sucesso.")
    void testInitAdoption_HappyPath() {
        User donor = createUser("1");
        User adopter = createUser("2");
        AdoptionRequestDto dto = createAdoptionRequestDto();
        Animal animal = createAnimal();

        when(animalRepository.findById("3")).thenReturn(Optional.of(animal));
        when(adoptionRepository.insert(any(Adoption.class))).thenAnswer(returnsFirstArg());
        when(animalRepository.save(any(Animal.class))).thenAnswer(returnsFirstArg());
        when(userRepository.findById("2")).thenReturn(Optional.of(donor));

        AdoptionResponseDto response = service.initAdoption(dto, adopter);

        assertNotNull(response);
        assertEquals(AdoptionStatus.PENDENTE, response.status());
        assertEquals("3", response.animal().id());
        assertEquals("1", response.animalOwner().id());
        assertEquals("2", response.adopter().id());
        assertEquals("Gostaria de um amigo novo.", response.reason());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());

        ArgumentCaptor<Animal> animalCaptor = ArgumentCaptor.forClass(Animal.class);
        verify(animalRepository).save(animalCaptor.capture());
        Animal savedEntity = animalCaptor.getValue();

        assertEquals(AdoptionStatus.PENDENTE, savedEntity.getStatus());
    }

    @Test
    @DisplayName("initAdoption: deve lançar exceção quando o animal informado não for encontrado.")
    void testInitAdoption_AnimalNotFound() {
        when(animalRepository.findById("3")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.initAdoption(createAdoptionRequestDto(), createUser("2")));
        verify(animalRepository, times(1)).findById("3");
        verify(adoptionRepository, never()).insert(any(Adoption.class));
        verify(animalRepository, never()).save(any(Animal.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("initAdoption: deve lançar exceção quando o usuário tentar adotar o próprio animal.")
    void testInitAdoption_UserTryingToAdoptTheirOwnAnimal() {
        User donor = createUser("1");
        AdoptionRequestDto dto = createAdoptionRequestDto();
        Animal animal = createAnimal();

        when(animalRepository.findById("3")).thenReturn(Optional.of(animal));
        doThrow(TryAdoptionYourOwnPetException.class)
                .when(tryAdoptionYourOwnPetValidation).validate(any(Animal.class), eq(donor));

        assertThrows(TryAdoptionYourOwnPetException.class,
                () -> service.initAdoption(dto, donor));
        verify(animalRepository, times(1)).findById("3");
        verify(tryAdoptionYourOwnPetValidation, times(1)).validate(any(Animal.class), eq(donor));
        verify(adoptionRepository, never()).insert(any(Adoption.class));
        verify(animalRepository, never()).save(any(Animal.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("initAdoption: deve lançar exceção quando o animal não estiver disponível para adoção.")
    void testInitAdoption_AnimalUnavailable() {
        User adopter = createUser("2");
        AdoptionRequestDto dto = createAdoptionRequestDto();
        Animal animal = createAnimal();
        animal.setStatus(AdoptionStatus.PENDENTE);

        when(animalRepository.findById("3")).thenReturn(Optional.of(animal));
        doThrow(AnimalNotAvailableException.class)
                .when(animalNotAvailableValidation).validate(eq(animal), isNull());

        assertThrows(AnimalNotAvailableException.class,
                () -> service.initAdoption(dto, adopter));
        verify(animalRepository, times(1)).findById("3");
        verify(animalNotAvailableValidation, times(1)).validate(eq(animal), isNull());
        verify(adoptionRepository, never()).insert(any(Adoption.class));
        verify(animalRepository, never()).save(any(Animal.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("initAdoption: deve lançar exceção quando o doador do animal estiver inativo.")
    void testInitAdoption_AnimalDonorInactive() {
        User donor = createUser("1");
        donor.setDeletedAt(ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString());
        User adopter = createUser("2");
        AdoptionRequestDto dto = createAdoptionRequestDto();
        Animal animal = createAnimal();

        when(animalRepository.findById("3")).thenReturn(Optional.of(animal));
        doThrow(UserInactiveException.class)
                .when(userActiveYetValidation).validate(any(User.class));

        assertThrows(UserInactiveException.class,
                () -> service.initAdoption(dto, adopter));
        verify(animalRepository, times(1)).findById("3");
        verify(userActiveYetValidation, times(1)).validate(any(User.class));
        verify(adoptionRepository, never()).insert(any(Adoption.class));
        verify(animalRepository, never()).save(any(Animal.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("getAdoptionsRequestedByMe: deve retornar as solicitações de adoção feitas pelo usuário autenticado.")
    void testGetAdoptionsRequestedByMe_HappyPath() {
        User authenticatedUser = createUser("1");
        User animalOwner = createUser("2");
        Animal animal = createAnimal();
        Adoption adoption = createAdoption();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Adoption> adoptionPage = new PageImpl<>(List.of(adoption), pageable, 1);

        when(adoptionRepository.findByAdopterId("1", pageable))
                .thenReturn(adoptionPage);
        when(userRepository.findAllById(Set.of("1", "2")))
                .thenReturn(List.of(authenticatedUser, animalOwner));
        when(animalRepository.findAllById(Set.of("3")))
                .thenReturn(List.of(animal));

        Page<AdoptionResponseDto> response = service.getAdoptionsRequestedByMe(pageable, authenticatedUser);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        AdoptionResponseDto dto = response.getContent().get(0);
        assertEquals("1", dto.id());
        assertEquals("3", dto.animal().id());
        assertEquals("2", dto.animalOwner().id());
        assertEquals("1", dto.adopter().id());

        verify(adoptionRepository, times(1)).findByAdopterId("1", pageable);
        verify(userRepository, times(1)).findAllById(Set.of("1", "2"));
        verify(animalRepository, times(1)).findAllById(Set.of("3"));
    }

    @Test
    @DisplayName("cancelAdoption: deve cancelar a adoção com sucesso.")
    void testCancelAdoption_HappyPath() {
        User authenticatedUser = createUser("1");
        Adoption adoption = createAdoption();

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        when(adoptionRepository.save(any(Adoption.class))).thenAnswer(returnsFirstArg());
        when(animalRepository.save(any(Animal.class))).thenAnswer(returnsFirstArg());
        when(userRepository.findById("2")).thenReturn(Optional.of(authenticatedUser));

        AdoptionResponseDto response = service.cancelAdoption("1", authenticatedUser);

        assertNotNull(response);
        assertEquals("1", response.id());
        assertEquals(AdoptionStatus.CANCELADO, response.status());
        assertNotEquals(adoption.getUpdatedAt(), response.updatedAt());

        ArgumentCaptor<Animal> animalCaptor = ArgumentCaptor.forClass(Animal.class);
        verify(animalRepository).save(animalCaptor.capture());
        Animal savedEntity = animalCaptor.getValue();

        assertEquals(AdoptionStatus.DISPONIVEL, savedEntity.getStatus());
    }

    @Test
    @DisplayName("cancelAdoption: deve lançar exceção ao tentar cancelar uma adoção inexistente.")
    void testCancelAdoption_AdoptionNotFound() {
        when(adoptionRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.cancelAdoption("1", createUser("1")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, never()).findById(any());
        verify(adopterOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("cancelAdoption: deve lançar exceção quando o animal da adoção não for encontrado.")
    void testCancelAdoption_AnimalNotFound() {
        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(animalRepository.findById("3")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.cancelAdoption("1", createUser("1")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(adopterOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("cancelAdoption: deve lançar exceção quando o usuário tentar cancelar a adoção de outro usuário.")
    void testCancelAdoption_UserTryingToCancelAnotherUserAdoption() {
        User authenticatedUser = createUser("2");
        Adoption adoption = createAdoption();

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(adoption));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        doThrow(UserNotOwnershipException.class)
                .when(adopterOwnershipValidation).validate(adoption, authenticatedUser);

        assertThrows(UserNotOwnershipException.class,
                () -> service.cancelAdoption("1", authenticatedUser));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(adopterOwnershipValidation, times(1)).validate(adoption, authenticatedUser);
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("cancelAdoption: deve lançar exceção ao tentar cancelar uma adoção já finalizada.")
    void testCancelAdoption_AttemptToCancelAdoptionAlreadyCompleted() {
        Adoption adoption = createAdoption();
        adoption.setStatus(AdoptionStatus.APROVADO);

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(adoption));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        doThrow(AdoptionAlreadyProcessedException.class)
                .when(pendingAdoptionValidation).validate(eq(adoption), isNull());

        assertThrows(AdoptionAlreadyProcessedException.class,
                () -> service.cancelAdoption("1", createUser("1")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(adopterOwnershipValidation, times(1)).validate(any(), any());
        verify(pendingAdoptionValidation, times(1)).validate(eq(adoption), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("acceptAdoption: deve aprovar a adoção com sucesso.")
    void testAcceptAdoption_HappyPath() {
        User authenticatedUser = createUser("2");
        Adoption adoption = createAdoption();

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        when(adoptionRepository.save(any(Adoption.class))).thenAnswer(returnsFirstArg());
        when(animalRepository.save(any(Animal.class))).thenAnswer(returnsFirstArg());
        when(userRepository.findById("1")).thenReturn(Optional.of(authenticatedUser));

        AdoptionResponseDto response = service.acceptAdoption("1", authenticatedUser);

        assertNotNull(response);
        assertEquals("1", response.id());
        assertEquals(AdoptionStatus.APROVADO, response.status());
        assertNotEquals(adoption.getUpdatedAt(), response.updatedAt());

        ArgumentCaptor<Animal> animalCaptor = ArgumentCaptor.forClass(Animal.class);
        verify(animalRepository).save(animalCaptor.capture());
        Animal savedEntity = animalCaptor.getValue();

        assertEquals(AdoptionStatus.ADOTADO, savedEntity.getStatus());
    }

    @Test
    @DisplayName("acceptAdoption: deve lançar exceção ao tentar aprovar uma adoção inexistente.")
    void testAcceptAdoption_AdoptionNotFound() {
        when(adoptionRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.acceptAdoption("1", createUser("2")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, never()).findById(any());
        verify(animalOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("acceptAdoption: deve lançar exceção quando o animal da adoção não for encontrado.")
    void testAcceptAdoption_AnimalNotFound() {
        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(animalRepository.findById("3")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.acceptAdoption("1", createUser("2")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(animalOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("acceptAdoption: deve lançar exceção quando o usuário tentar aprovar adoção de animal que não possui.")
    void testAcceptAdoption_UserTryingToApproveAnotherUserAdoption() {
        User authenticatedUser = createUser("2");
        Adoption adoption = createAdoption();

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(adoption));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        doThrow(UserNotOwnershipException.class)
                .when(animalOwnershipValidation).validate(adoption, authenticatedUser);

        assertThrows(UserNotOwnershipException.class,
                () -> service.acceptAdoption("1", authenticatedUser));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(animalOwnershipValidation, times(1)).validate(adoption, authenticatedUser);
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("acceptAdoption: deve lançar exceção ao tentar aprovar uma adoção já processada.")
    void testAcceptAdoption_AttemptToApproveAdoptionAlreadyCompleted() {
        Adoption adoption = createAdoption();
        adoption.setStatus(AdoptionStatus.RECUSADO);

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(adoption));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        doThrow(AdoptionAlreadyProcessedException.class)
                .when(pendingAdoptionValidation).validate(eq(adoption), isNull());

        assertThrows(AdoptionAlreadyProcessedException.class,
                () -> service.acceptAdoption("1", createUser("2")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(animalOwnershipValidation, times(1)).validate(any(), any());
        verify(pendingAdoptionValidation, times(1)).validate(eq(adoption), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("denyAdoption: deve recusar a adoção com sucesso.")
    void testDenyAdoption_HappyPath() {
        User authenticatedUser = createUser("2");
        Adoption adoption = createAdoption();

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        when(adoptionRepository.save(any(Adoption.class))).thenAnswer(returnsFirstArg());
        when(animalRepository.save(any(Animal.class))).thenAnswer(returnsFirstArg());
        when(userRepository.findById("1")).thenReturn(Optional.of(authenticatedUser));

        AdoptionResponseDto response = service.denyAdoption("1", authenticatedUser);

        assertNotNull(response);
        assertEquals("1", response.id());
        assertEquals(AdoptionStatus.RECUSADO, response.status());
        assertNotEquals(adoption.getUpdatedAt(), response.updatedAt());

        ArgumentCaptor<Animal> animalCaptor = ArgumentCaptor.forClass(Animal.class);
        verify(animalRepository).save(animalCaptor.capture());
        Animal savedEntity = animalCaptor.getValue();

        assertEquals(AdoptionStatus.DISPONIVEL, savedEntity.getStatus());
    }

    @Test
    @DisplayName("denyAdoption: deve lançar exceção ao tentar recusar uma adoção inexistente.")
    void testDenyAdoption_AdoptionNotFound() {
        when(adoptionRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.denyAdoption("1", createUser("2")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, never()).findById(any());
        verify(animalOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("denyAdoption: deve lançar exceção quando o animal da adoção não for encontrado.")
    void testDenyAdoption_AnimalNotFound() {
        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(animalRepository.findById("3")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.denyAdoption("1", createUser("2")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(animalOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("denyAdoption: deve lançar exceção quando o usuário tentar recusar adoção de animal que não possui.")
    void testDenyAdoption_UserTryingToDenyAnotherUserAdoption() {
        User authenticatedUser = createUser("2");
        Adoption adoption = createAdoption();

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(adoption));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        doThrow(UserNotOwnershipException.class)
                .when(animalOwnershipValidation).validate(adoption, authenticatedUser);

        assertThrows(UserNotOwnershipException.class,
                () -> service.denyAdoption("1", authenticatedUser));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(animalOwnershipValidation, times(1)).validate(adoption, authenticatedUser);
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("denyAdoption: deve lançar exceção ao tentar recusar uma adoção já processada.")
    void testDenyAdoption_AttemptToDenyAdoptionAlreadyCompleted() {
        Adoption adoption = createAdoption();
        adoption.setStatus(AdoptionStatus.APROVADO);

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(adoption));
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        doThrow(AdoptionAlreadyProcessedException.class)
                .when(pendingAdoptionValidation).validate(eq(adoption), isNull());

        assertThrows(AdoptionAlreadyProcessedException.class,
                () -> service.denyAdoption("1", createUser("2")));
        verify(adoptionRepository, times(1)).findById("1");
        verify(animalRepository, times(1)).findById("3");
        verify(animalOwnershipValidation, times(1)).validate(any(), any());
        verify(pendingAdoptionValidation, times(1)).validate(eq(adoption), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("editReason: deve permitir a edição do motivo da adoção quando a solicitação estiver pendente.")
    void testEditReason_HappyPath() {
        User authenticatedUser = createUser("1");

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        when(adoptionRepository.save(any(Adoption.class))).thenAnswer(returnsFirstArg());
        when(animalRepository.findById("3")).thenReturn(Optional.of(createAnimal()));
        when(userRepository.findById("2")).thenReturn(Optional.of(createUser("2")));

        AdoptionResponseDto response =
                service.editReason("1", new EditReasonDto("Possuo um quintal amplo."), authenticatedUser);

        assertNotNull(response);
        assertEquals("Possuo um quintal amplo.", response.reason());
    }

    @Test
    @DisplayName("editReason: deve lançar exceção ao tentar editar o motivo de uma adoção inexistente.")
    void testEditReason_AdoptionNotFound() {
        User authenticatedUser = createUser("1");

        when(adoptionRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.editReason("1", new EditReasonDto("Possuo um quintal amplo."), authenticatedUser));
        verify(adoptionRepository).findById("1");
        verify(adopterOwnershipValidation, never()).validate(any(), any());
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("editReason: deve lançar exceção quando o usuário tentar editar a adoção de outro usuário.")
    void testEditReason_UserTryingToEditAnotherUserAdoption() {
        User authenticatedUser = createUser("2");

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        doThrow(UserNotOwnershipException.class)
                .when(adopterOwnershipValidation).validate(any(Adoption.class), eq(authenticatedUser));

        assertThrows(UserNotOwnershipException.class,
                () -> service.editReason("1", new EditReasonDto("Possuo um quintal amplo."), authenticatedUser));
        verify(adoptionRepository).findById("1");
        verify(adopterOwnershipValidation, times(1)).validate(any(Adoption.class), eq(authenticatedUser));
        verify(pendingAdoptionValidation, never()).validate(any(), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("editReason: deve lançar exceção ao tentar editar o motivo de uma adoção já processada.")
    void testEditReason_AttemptToEditAdoptionAlreadyCompleted() {
        User authenticatedUser = createUser("1");

        when(adoptionRepository.findById("1")).thenReturn(Optional.of(createAdoption()));
        doThrow(AdoptionAlreadyProcessedException.class)
                .when(pendingAdoptionValidation).validate(any(Adoption.class), isNull());

        assertThrows(AdoptionAlreadyProcessedException.class,
                () -> service.editReason("1", new EditReasonDto("Possuo um quintal amplo."), authenticatedUser));
        verify(adoptionRepository).findById("1");
        verify(adopterOwnershipValidation, times(1)).validate(any(Adoption.class), eq(authenticatedUser));
        verify(pendingAdoptionValidation, times(1)).validate(any(Adoption.class), isNull());
        verify(adoptionRepository, never()).save(any());
        verify(animalRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("getRequestsForMyAnimals: deve retornar as solicitações de adoção recebidas para os animais do usuário.")
    void testGetRequestsForMyAnimals_HappyPath() {
        User authenticatedUser = createUser("2");
        User adopter = createUser("1");
        Animal animal = createAnimal();
        Adoption adoption = createAdoption();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Adoption> adoptionPage = new PageImpl<>(List.of(adoption), pageable, 1);

        when(adoptionRepository.findByAnimalOwnerId("2", pageable))
                .thenReturn(adoptionPage);
        when(userRepository.findAllById(Set.of("1", "2")))
                .thenReturn(List.of(authenticatedUser, adopter));
        when(animalRepository.findAllById(Set.of("3")))
                .thenReturn(List.of(animal));

        Page<AdoptionResponseDto> response = service.getRequestsForMyAnimals(pageable, authenticatedUser);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        AdoptionResponseDto dto = response.getContent().get(0);
        assertEquals("1", dto.id());
        assertEquals("3", dto.animal().id());
        assertEquals("2", dto.animalOwner().id());
        assertEquals("1", dto.adopter().id());

        verify(adoptionRepository, times(1)).findByAnimalOwnerId("2", pageable);
        verify(userRepository, times(1)).findAllById(Set.of("1", "2"));
        verify(animalRepository, times(1)).findAllById(Set.of("3"));
    }

    Adoption createAdoption() {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return Adoption.builder()
                .id("1")
                .status(AdoptionStatus.PENDENTE)
                .animalId("3")
                .animalOwnerId("2")
                .adopterId("1")
                .reason("Gostaria de um amigo novo.")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }

    AdoptionRequestDto createAdoptionRequestDto() {
        return new AdoptionRequestDto(
                "3",
                "Gostaria de um amigo novo."
        );
    }

    User createUser(String id) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return User.builder()
                .id(id)
                .email("user@teste.com")
                .password("secretPassword")
                .name("Teste")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }

    Animal createAnimal() {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return Animal.builder()
                .id("3")
                .name("Rex")
                .birthDate(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(AnimalType.CACHORRO)
                .sex(AnimalSex.MACHO)
                .size(AnimalSize.PEQUENO)
                .status(AdoptionStatus.DISPONIVEL)
                .authorId("2")
                .description("Animal docil")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}