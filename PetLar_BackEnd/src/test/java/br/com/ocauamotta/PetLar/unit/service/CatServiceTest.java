package br.com.ocauamotta.PetLar.unit.service;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mapper.CatMapper;
import br.com.ocauamotta.PetLar.repository.ICatRepository;
import br.com.ocauamotta.PetLar.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatServiceTest {
    @Mock
    private ICatRepository repository;

    @InjectMocks
    private CatService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedCat() {
        Cat entity = cat();
        when(repository.insert(any(Cat.class))).thenReturn(entity);

        AnimalDTO saved = service.save(createAnimalDTO());

        assertEquals("Mimi", saved.getName());
        assertEquals("Gato", saved.getType());
        verify(repository, times(1)).insert(any(Cat.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedCat() {
        Cat entity = cat();
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(repository.save(any(Cat.class))).thenReturn(entity);

        AnimalDTO dto = CatMapper.toDTO(entity);
        AnimalDTO updated = service.update(dto);

        assertEquals("Mimi", updated.getName());
        verify(repository, times(1)).save(any(Cat.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete_WhenCatExists() {
        Cat entity = cat();
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        service.delete("1");

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_ShouldNotCallRepositoryDelete_WhenCatDoesNotExist() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        service.delete("1");

        verify(repository, never()).delete(any(Cat.class));
    }

    @Test
    void testFindById_ShouldReturnCat_WhenFound() {
        Cat entity = cat();
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        AnimalDTO dto = service.findById("1");

        assertEquals("Mimi", dto.getName());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void testFindById_ShouldThrowException_WhenNotFound() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));
    }

    @Test
    void testFindAll_ShouldReturnPageOfCat() {
        Cat entity = cat();
        Page<Cat> page = new PageImpl<>(List.of(entity));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("Mimi", result.getContent().get(0).getName());
    }

    @Test
    void testFindAll_ShouldReturnPageOfCat_WithStatusAvailable() {
        Cat entity = cat();
        Page<Cat> page = new PageImpl<>(List.of(entity));
        when(repository.findByStatus(eq(AdoptionStatus.AVAILABLE),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Disponível");

        assertEquals(1, result.getTotalElements());
        assertEquals("Mimi", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.AVAILABLE.getLabel(), result.getContent().get(0).getStatus());
    }

    @Test
    void testFindAll_ShouldReturnPageOfCat_WithStatusAdopted() {
        Cat entity = cat();
        entity.setStatus(AdoptionStatus.ADOPTED);
        Page<Cat> page = new PageImpl<>(List.of(entity));
        when(repository.findByStatus(eq(AdoptionStatus.ADOPTED),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Adotado");

        assertEquals(1, result.getTotalElements());
        assertEquals("Mimi", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.ADOPTED.getLabel(), result.getContent().get(0).getStatus());
    }

    Cat cat() {
        return Cat.builder()
                .id("1")
                .name("Mimi")
                .age(12)
                .type(AnimalType.CAT)
                .breed("SRD")
                .sex(AnimalSex.FEMALE)
                .weight(400)
                .size(AnimalSize.SMALL)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Carinhosa.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Mimi")
                .age(12)
                .breed("SRD")
                .sex(AnimalSex.FEMALE.getLabel())
                .weight(400)
                .size(AnimalSize.SMALL.getLabel())
                .description("Carinhosa.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
