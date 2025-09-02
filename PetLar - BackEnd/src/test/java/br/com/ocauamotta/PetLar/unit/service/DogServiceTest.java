package br.com.ocauamotta.PetLar.unit.service;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mapper.DogMapper;
import br.com.ocauamotta.PetLar.repository.IDogRepository;
import br.com.ocauamotta.PetLar.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DogServiceTest {
    @Mock
    private IDogRepository repository;

    @InjectMocks
    private DogService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedDogDTO() {
        Dog dogEntity = dog();
        when(repository.insert(any(Dog.class))).thenReturn(dogEntity);

        AnimalDTO saved = service.save(createAnimalDTO());

        assertEquals("Rex", saved.getName());
        assertEquals("Cachorro", saved.getType());
        verify(repository, times(1)).insert(any(Dog.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedDogDTO() {
        Dog dogEntity = dog();
        when(repository.findById("1")).thenReturn(Optional.of(dogEntity));
        when(repository.save(any(Dog.class))).thenReturn(dogEntity);

        AnimalDTO dto = DogMapper.toDTO(dogEntity);
        AnimalDTO updated = service.update(dto);

        assertEquals("Rex", updated.getName());
        verify(repository, times(1)).save(any(Dog.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete_WhenDogExists() {
        Dog dogEntity = dog();
        when(repository.findById("1")).thenReturn(Optional.of(dogEntity));

        service.delete("1");

        verify(repository, times(1)).delete(dogEntity);
    }

    @Test
    void testDelete_ShouldNotCallRepositoryDelete_WhenDogDoesNotExist() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        service.delete("1");

        verify(repository, never()).delete(any(Dog.class));
    }

    @Test
    void testFindById_ShouldReturnDogDTO_WhenFound() {
        Dog dogEntity = dog();
        when(repository.findById("1")).thenReturn(Optional.of(dogEntity));

        AnimalDTO dto = service.findById("1");

        assertEquals("Rex", dto.getName());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void testFindById_ShouldThrowException_WhenNotFound() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));
    }

    @Test
    void testFindAll_ShouldReturnPageOfDogDTOs() {
        Dog dogEntity = dog();
        Page<Dog> page = new PageImpl<>(List.of(dogEntity));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).getName());
    }

    @Test
    void testFindAll_ShouldReturnPageOfDog_WithStatusAvailable() {
        Dog dogEntity = dog();
        Page<Dog> page = new PageImpl<>(List.of(dogEntity));
        when(repository.findByStatus(eq(AdoptionStatus.AVAILABLE),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Disponível");

        assertEquals(1, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.AVAILABLE.getLabel(), result.getContent().get(0).getStatus());
    }

    @Test
    void testFindAll_ShouldReturnPageOfDog_WithStatusAdopted() {
        Dog dogEntity = dog();
        dogEntity.setStatus(AdoptionStatus.ADOPTED);
        Page<Dog> page = new PageImpl<>(List.of(dogEntity));
        when(repository.findByStatus(eq(AdoptionStatus.ADOPTED),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Adotado");

        assertEquals(1, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.ADOPTED.getLabel(), result.getContent().get(0).getStatus());
    }

    Dog dog() {
        return Dog.builder()
                .id("1")
                .name("Rex")
                .age(3)
                .type(AnimalType.DOG)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Rex")
                .age(3)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE.getLabel())
                .weight(10)
                .size(AnimalSize.MEDIUM.getLabel())
                .description("Cão amigável")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
