package br.com.ocauamotta.PetLar.unit.service;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mapper.BirdMapper;
import br.com.ocauamotta.PetLar.repository.IBirdRepository;
import br.com.ocauamotta.PetLar.service.BirdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BirdServiceTest {
    @Mock
    private IBirdRepository repository;

    @InjectMocks
    private BirdService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedBird() {
        Bird entity = bird();
        when(repository.insert(any(Bird.class))).thenReturn(entity);

        AnimalDTO saved = service.save(createAnimalDTO());

        assertEquals("Loro", saved.getName());
        assertEquals("Ave", saved.getType());
        verify(repository, times(1)).insert(any(Bird.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedBird() {
        Bird entity = bird();
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(repository.save(any(Bird.class))).thenReturn(entity);

        AnimalDTO dto = BirdMapper.toDTO(entity);
        AnimalDTO updated = service.update(dto);

        assertEquals("Loro", updated.getName());
        verify(repository, times(1)).save(any(Bird.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete_WhenBirdExists() {
        Bird entity = bird();
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        service.delete("1");

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_ShouldNotCallRepositoryDelete_WhenBirdDoesNotExist() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        service.delete("1");

        verify(repository, never()).delete(any(Bird.class));
    }

    @Test
    void testFindById_ShouldReturnBird_WhenFound() {
        Bird entity = bird();
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        AnimalDTO dto = service.findById("1");

        assertEquals("Loro", dto.getName());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void testFindById_ShouldThrowException_WhenNotFound() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));
    }

    @Test
    void testFindAll_ShouldReturnPageOfBird() {
        Bird entity = bird();
        Page<Bird> page = new PageImpl<>(List.of(entity));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("Loro", result.getContent().get(0).getName());
    }

    @Test
    void testFindAll_ShouldReturnPageOfBird_WithStatusAvailable() {
        Bird entity = bird();
        Page<Bird> page = new PageImpl<>(List.of(entity));
        when(repository.findByStatus(eq(AdoptionStatus.AVAILABLE),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Disponível");

        assertEquals(1, result.getTotalElements());
        assertEquals("Loro", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.AVAILABLE.getLabel(), result.getContent().get(0).getStatus());
    }

    @Test
    void testFindAll_ShouldReturnPageOfBird_WithStatusAdopted() {
        Bird entity = bird();
        entity.setStatus(AdoptionStatus.ADOPTED);
        Page<Bird> page = new PageImpl<>(List.of(entity));
        when(repository.findByStatus(eq(AdoptionStatus.ADOPTED),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Adotado");

        assertEquals(1, result.getTotalElements());
        assertEquals("Loro", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.ADOPTED.getLabel(), result.getContent().get(0).getStatus());
    }

    Bird bird() {
        return Bird.builder()
                .id("1")
                .name("Loro")
                .age(12)
                .type(AnimalType.BIRD)
                .breed("Papagaio")
                .sex(AnimalSex.MALE)
                .weight(50)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Pássaro alegre e falador.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Loro")
                .age(12)
                .breed("Papagaio")
                .sex(AnimalSex.MALE.getLabel())
                .weight(50)
                .size(AnimalSize.SMALL.getLabel())
                .description("Pássaro alegre e falador.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
