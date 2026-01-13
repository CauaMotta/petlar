package br.com.ocauamotta.PetLar.services.Animal;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AnimalNotAvailableException;
import br.com.ocauamotta.PetLar.exceptions.Animal.UserWhoIsNotTheOwnerOfTheAnimalException;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.services.AnimalService;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalNotAvailableValidation;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalOwnerUserValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private IAnimalRepository repository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private AnimalOwnerUserValidation animalOwnerUserValidation;

    @Mock
    private AnimalNotAvailableValidation animalNotAvailableValidation;

    @InjectMocks
    private AnimalService service;

    @Test
    @DisplayName("Deve salvar um animal com sucesso.")
    void testSave_ShouldSaveNewAnimal() {
        AnimalRequestDto dto = createAnimalRequestDto("Rex", "Cachorro", "Macho");

        when(repository.insert(any(Animal.class))).thenAnswer(returnsFirstArg());

        AnimalResponseDto savedAnimalDto = service.save(dto, null, createUser("1"));

        assertNotNull(savedAnimalDto);
        assertEquals("Rex", savedAnimalDto.name());
        assertNotNull(savedAnimalDto.birthDate());
        assertEquals(AdoptionStatus.DISPONIVEL, savedAnimalDto.status());
        assertNotNull(savedAnimalDto.createdAt());
        assertNotNull(savedAnimalDto.updatedAt());
        verify(repository).insert(any(Animal.class));
    }

    @Test
    @DisplayName("Deve atualizar um animal com sucesso.")
    void testUpdate_ShouldUpdateAnAnimal() {
        AnimalRequestDto dto = createAnimalRequestDto("Luna", "Gato", "Femea");

        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));
        when(repository.save(any(Animal.class))).thenAnswer(returnsFirstArg());

        AnimalResponseDto updatedAnimalDto = service.update("1", dto, null, createUser("1"));

        assertNotNull(updatedAnimalDto);
        assertEquals("Luna", updatedAnimalDto.name());
        assertEquals(AnimalType.GATO, updatedAnimalDto.type());
        verify(repository).findById("1");
        verify(repository).save((any(Animal.class)));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o metodo UPDATE for chamado com ID inexistente")
    void testUpdate_ShouldThrowEntityNotFoundException() {
        AnimalRequestDto dto = createAnimalRequestDto("Luna", "Gato", "Femea");

        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.update("1", dto, null, createUser("1")));
        verify(repository).findById("1");
        verifyNoInteractions(animalOwnerUserValidation, animalNotAvailableValidation);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário tentar atualizar um animal que não o pertence.")
    void testUpdate_ShouldThrowUserWhoIsNotTheOwnerOfTheAnimalException() {
        AnimalRequestDto dto = createAnimalRequestDto("Luna", "Gato", "Femea");

        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));
        doThrow(UserWhoIsNotTheOwnerOfTheAnimalException.class)
                .when(animalOwnerUserValidation).validate(any(Animal.class), any(User.class));

        assertThrows(UserWhoIsNotTheOwnerOfTheAnimalException.class,
                () -> service.update("1", dto, null, createUser("2")));
        verify(repository).findById("1");
        verify(animalOwnerUserValidation).validate(any(Animal.class), any(User.class));
        verifyNoInteractions(animalNotAvailableValidation);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário tentar atualizar um animal que não está disponível.")
    void testUpdate_ShouldThrowAnimalNotAvailableException() {
        AnimalRequestDto dto = createAnimalRequestDto("Luna", "Gato", "Femea");

        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));
        doThrow(AnimalNotAvailableException.class)
                .when(animalNotAvailableValidation).validate(any(Animal.class), isNull());

        assertThrows(AnimalNotAvailableException.class,
                () -> service.update("1", dto, null, createUser("1")));
        verify(repository).findById("1");
        verify(animalOwnerUserValidation).validate(any(Animal.class), any(User.class));
        verify(animalNotAvailableValidation).validate(any(Animal.class), isNull());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve deletar um animal com sucesso.")
    void testDelete_ShouldDeleteAnAnimal() {
        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));

        service.delete("1", createUser("1"));

        verify(repository).findById("1");
        verify(repository).delete(any(Animal.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o metodo DELETE for chamado com ID inexistente.")
    void testDelete_ShouldThrowEntityNotFoundException() {
        when(repository.findById("1")).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class,
                () -> service.delete("1", createUser("1")));
        verify(repository).findById("1");
        verifyNoInteractions(animalOwnerUserValidation, animalNotAvailableValidation);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário tentar deletar um animal que não o pertence.")
    void testDelete_ShouldThrowUserWhoIsNotTheOwnerOfTheAnimalException() {
        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));

        doThrow(UserWhoIsNotTheOwnerOfTheAnimalException.class)
                .when(animalOwnerUserValidation).validate(any(Animal.class), any(User.class));

        assertThrows(UserWhoIsNotTheOwnerOfTheAnimalException.class,
                () -> service.delete("1", createUser("1")));
        verify(repository).findById("1");
        verify(animalOwnerUserValidation, times(1)).validate(any(Animal.class), any(User.class));
        verifyNoInteractions(animalNotAvailableValidation);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário tentar deletar um animal indisponível.")
    void testDelete_ShouldThrowAnimalNotAvailableException() {
        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));

        doThrow(AnimalNotAvailableException.class)
                .when(animalNotAvailableValidation).validate(any(Animal.class), isNull());

        assertThrows(AnimalNotAvailableException.class,
                () -> service.delete("1", createUser("1")));
        verify(repository).findById("1");
        verify(animalOwnerUserValidation).validate(any(Animal.class), any(User.class));
        verify(animalNotAvailableValidation).validate(any(Animal.class), isNull());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Deve buscar um animal por ID com sucesso.")
    void testFindById_ShouldReturnAnAnimal() {
        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));
        when(userRepository.findById("1")).thenReturn(Optional.of(createUser("1")));

        AnimalResponseDto foundAnimal = service.findById("1");

        assertNotNull(foundAnimal);
        assertEquals("1", foundAnimal.id());
        verify(repository).findById("1");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o metodo BUSCAR for chamado com ID inexistente.")
    void testFindById_ShouldThrowEntityNotFoundException() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais sem filtro de espécie.")
    void testFindAll_ShouldReturnAPageOfAnimals() {
        Animal dogEntity = createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO);
        Animal catEntity = createAnimal("2", "Lua", AnimalType.GATO, AnimalSex.FEMEA);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Animal> page = new PageImpl<>(List.of(dogEntity, catEntity));

        when(repository.findByStatus(eq(AdoptionStatus.DISPONIVEL), any(Pageable.class))).thenReturn(page);
        when(userRepository.findAllById(Set.of("1"))).thenReturn(List.of(createUser("1")));

        Page<AnimalResponseDto> result = service.findAll(pageable, "Disponivel", null);

        assertEquals(2, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).name());
        assertEquals("Lua", result.getContent().get(1).name());
        verify(repository).findByStatus(eq(AdoptionStatus.DISPONIVEL), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com filtro de espécie gatos.")
    void testFindAll_ShouldReturnAPageOfCats() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Animal> page = new PageImpl<>(List.of(createAnimal("2", "Lua", AnimalType.GATO, AnimalSex.FEMEA)));

        when(repository.findByStatusAndType(eq(AdoptionStatus.DISPONIVEL), eq(AnimalType.GATO), any(Pageable.class)))
                .thenReturn(page);
        when(userRepository.findAllById(Set.of("1"))).thenReturn(List.of(createUser("1")));

        Page<AnimalResponseDto> result = service.findAll(pageable, "Disponivel", "Gato");

        assertEquals(1, result.getTotalElements());
        assertEquals("Lua", result.getContent().getFirst().name());
        verify(repository).findByStatusAndType(eq(AdoptionStatus.DISPONIVEL), eq(AnimalType.GATO), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com filtro de status adotado.")
    void testFindAll_ShouldReturnAPageOfAdoptedAnimals() {
        Animal dogEntity = createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO);
        Animal catEntity = createAnimal("2", "Lua", AnimalType.GATO, AnimalSex.FEMEA);
        dogEntity.setStatus(AdoptionStatus.ADOTADO);
        catEntity.setStatus(AdoptionStatus.ADOTADO);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Animal> page = new PageImpl<>(List.of(dogEntity, catEntity));

        when(repository.findByStatus(eq(AdoptionStatus.ADOTADO), any(Pageable.class))).thenReturn(page);
        when(userRepository.findAllById(Set.of("1"))).thenReturn(List.of(createUser("1")));

        Page<AnimalResponseDto> result = service.findAll(pageable, "Adotado", null);

        assertEquals(2, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).name());
        assertEquals("Lua", result.getContent().get(1).name());
        assertEquals(AdoptionStatus.ADOTADO, result.getContent().get(0).status());
        assertEquals(AdoptionStatus.ADOTADO, result.getContent().get(1).status());
        verify(repository).findByStatus(eq(AdoptionStatus.ADOTADO), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao BUSCAR com FILTROS inválidos.")
    void testFindAll_ShouldThrowIllegalArgumentException() {
        PageRequest pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () -> service.findAll(pageable, "available", null));
    }

    AnimalRequestDto createAnimalRequestDto(String name, String type, String sex) {
        return new AnimalRequestDto(
                name,
                LocalDate.of(2025, 10, 10),
                1200,
                type,
                sex,
                "pequeno",
                "Animal docil"
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

    Animal createAnimal(String id, String name, AnimalType type, AnimalSex sex) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return Animal.builder()
                .id(id)
                .name(name)
                .birthDate(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(type)
                .sex(sex)
                .size(AnimalSize.PEQUENO)
                .status(AdoptionStatus.DISPONIVEL)
                .authorId("1")
                .description("Animal docil")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}