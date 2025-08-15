package br.com.ocauamotta.PetLar.unit.service;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
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
    private static DogService service;

    @BeforeEach
    void setUpClass() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedDogDTO() {
        Dog dogEntity = createDogEntity();
        when(repository.insert(any(Dog.class))).thenReturn(dogEntity);

        DogDTO saved = service.save(createDogDTO());

        assertEquals("Rex", saved.getName());
        assertEquals(AnimalType.DOG, saved.getType());
        verify(repository, times(1)).insert(any(Dog.class));
    }

    @Test
    void testUpdate_ShouldReturnUpdatedDogDTO() {
        Dog dogEntity = createDogEntity();
        when(repository.save(any(Dog.class))).thenReturn(dogEntity);

        DogDTO dto = DogMapper.toDTO(dogEntity);
        DogDTO updated = service.update(dto);

        assertEquals("Rex", updated.getName());
        verify(repository, times(1)).save(any(Dog.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete_WhenDogExists() {
        Dog dogEntity = createDogEntity();
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
        Dog dogEntity = createDogEntity();
        when(repository.findById("1")).thenReturn(Optional.of(dogEntity));

        DogDTO dto = service.findById("1");

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
        Dog dogEntity = createDogEntity();
        Page<Dog> page = new PageImpl<>(List.of(dogEntity));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DogDTO> result = service.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).getName());
    }

    Dog createDogEntity() {
        return Dog.builder()
                .id("1")
                .name("Rex")
                .yearsOld(3)
                .type(AnimalType.DOG)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .registrationDate(LocalDate.now())
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }

    CreateDogDTO createDogDTO() {
        return CreateDogDTO.builder()
                .name("Rex")
                .yearsOld(3)
                .breed("Vira-lata")
                .sex(AnimalSex.MALE)
                .weight(10)
                .size(AnimalSize.MEDIUM)
                .status(AdoptionStatus.AVAILABLE)
                .description("Cão amigável")
                .urlImage("url.com/img")
                .build();
    }
}
