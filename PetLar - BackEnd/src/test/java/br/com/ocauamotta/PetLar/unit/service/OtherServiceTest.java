package br.com.ocauamotta.PetLar.unit.service;

import br.com.ocauamotta.PetLar.domain.Other;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mapper.OtherMapper;
import br.com.ocauamotta.PetLar.repository.IOtherRepository;
import br.com.ocauamotta.PetLar.service.OtherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtherServiceTest {
    @Mock
    private IOtherRepository repository;

    @InjectMocks
    private OtherService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedOther() {
        Other entity = other();
        when(repository.insert(any(Other.class))).thenReturn(entity);

        AnimalDTO saved = service.save(createAnimalDTO());

        assertEquals("Bolt", saved.getName());
        assertEquals("Outro", saved.getType());
        verify(repository, times(1)).insert(any(Other.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedOther() {
        Other entity = other();
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(repository.save(any(Other.class))).thenReturn(entity);

        AnimalDTO dto = OtherMapper.toDTO(entity);
        AnimalDTO updated = service.update(dto);

        assertEquals("Bolt", updated.getName());
        verify(repository, times(1)).save(any(Other.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete_WhenOtherExists() {
        Other entity = other();
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        service.delete("1");

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_ShouldNotCallRepositoryDelete_WhenOtherDoesNotExist() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        service.delete("1");

        verify(repository, never()).delete(any(Other.class));
    }

    @Test
    void testFindById_ShouldReturnOther_WhenFound() {
        Other entity = other();
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        AnimalDTO dto = service.findById("1");

        assertEquals("Bolt", dto.getName());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void testFindById_ShouldThrowException_WhenNotFound() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));
    }

    @Test
    void testFindAll_ShouldReturnPageOfOther() {
        Other entity = other();
        Page<Other> page = new PageImpl<>(List.of(entity));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, null);

        assertEquals(1, result.getTotalElements());
        assertEquals("Bolt", result.getContent().get(0).getName());
    }

    @Test
    void testFindAll_ShouldReturnPageOfOther_WithStatusAvailable() {
        Other entity = other();
        Page<Other> page = new PageImpl<>(List.of(entity));
        when(repository.findByStatus(eq(AdoptionStatus.AVAILABLE),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Disponível");

        assertEquals(1, result.getTotalElements());
        assertEquals("Bolt", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.AVAILABLE.getLabel(), result.getContent().get(0).getStatus());
    }

    @Test
    void testFindAll_ShouldReturnPageOfOther_WithStatusAdopted() {
        Other entity = other();
        entity.setStatus(AdoptionStatus.ADOPTED);
        Page<Other> page = new PageImpl<>(List.of(entity));
        when(repository.findByStatus(eq(AdoptionStatus.ADOPTED),any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalDTO> result = service.findAll(pageable, "Adotado");

        assertEquals(1, result.getTotalElements());
        assertEquals("Bolt", result.getContent().get(0).getName());
        assertEquals(AdoptionStatus.ADOPTED.getLabel(), result.getContent().get(0).getStatus());
    }

    Other other() {
        return Other.builder()
                .id("1")
                .name("Bolt")
                .age(48)
                .type(AnimalType.OTHER)
                .breed("Coelho")
                .sex(AnimalSex.MALE)
                .weight(250)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Animal dócil e brincalhão.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }

    CreateAnimalDTO createAnimalDTO() {
        return CreateAnimalDTO.builder()
                .name("Bolt")
                .age(48)
                .breed("Coelho")
                .sex(AnimalSex.MALE.getLabel())
                .weight(250)
                .size(AnimalSize.MEDIUM.getLabel())
                .description("Animal dócil e brincalhão.")
                .urlImage("url.com/img")
                .author("Teste")
                .phone("11988776655")
                .build();
    }
}
