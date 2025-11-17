package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private IAnimalRepository repository;

    @InjectMocks
    private AnimalService service;

    @Test
    @DisplayName("Deve salvar um animal com sucesso")
    void testSave_ShouldSaveNewAnimal() {
        AnimalRequestDto dto = createAnimalRequestDto("Rex", "Cachorro", "Macho");
        when(repository.insert(Mockito.any(Animal.class))).thenReturn(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO));

        AnimalResponseDto savedAnimalDto = service.save(dto);

        assertNotNull(savedAnimalDto);
        assertEquals(dto.name(), savedAnimalDto.name());
        assertNotNull(savedAnimalDto.registrationDate());
        assertEquals(AdoptionStatus.DISPONIVEL, savedAnimalDto.status());
        verify(repository, times(1)).insert(Mockito.any(Animal.class));
    }

    @Test
    @DisplayName("Deve atualizar um animal com sucesso")
    void testUpdate_ShouldUpdateAnAnimal() {
        AnimalRequestDto dto = createAnimalRequestDto("Luna", "Gato", "Femea");
        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));
        when(repository.save(Mockito.any(Animal.class))).thenReturn(createAnimal("1", "Luna", AnimalType.GATO, AnimalSex.FEMEA));

        AnimalResponseDto updatedAnimalDto = service.update("1", dto);

        assertNotNull(updatedAnimalDto);
        assertEquals(dto.name(), updatedAnimalDto.name());
        assertTrue(dto.type().equalsIgnoreCase(updatedAnimalDto.type().toString()));
        verify(repository, times(1)).findById("1");
        verify(repository,times(1)).save((Mockito.any(Animal.class)));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o metodo UPDATE for chamado com ID inexistente")
    void testUpdate_ShouldThrowEntityNotFoundException() {
        AnimalRequestDto dto = createAnimalRequestDto("Luna", "Gato", "Femea");
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update("1", dto));
    }

    @Test
    @DisplayName("Deve deletar um animal com sucesso")
    void testDelete_ShouldDeleteAnAnimal() {
        when(repository.existsById("1")).thenReturn(true);

        service.delete("1");

        verify(repository, times(1)).existsById("1");
        verify(repository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o metodo DELETE for chamado com ID inexistente")
    void testDelete_ShouldThrowEntityNotFoundException() {
        when(repository.existsById("1")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete("1"));
    }

    @Test
    @DisplayName("Deve buscar um animal por ID com sucesso")
    void testFindById_ShouldReturnAnAnimal() {
        when(repository.findById("1")).thenReturn(Optional.of(createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO)));

        AnimalResponseDto foundAnimal = service.findById("1");

        assertNotNull(foundAnimal);
        assertEquals("1", foundAnimal.id());
        verify(repository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o metodo BUSCAR for chamado com ID inexistente")
    void testFindById_ShouldThrowEntityNotFoundException() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais sem filtro de espécie")
    void testFindAll_ShouldReturnAPageOfAnimals() {
        Animal dogEntity = createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO);
        Animal catEntity = createAnimal("2", "Lua", AnimalType.GATO, AnimalSex.FEMEA);
        Page<Animal> page = new PageImpl<>(List.of(dogEntity, catEntity));
        when(repository.findByStatus(eq(AdoptionStatus.DISPONIVEL), Mockito.any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalResponseDto> result = service.findAll(pageable, "Disponivel", null);

        assertEquals(2, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).name());
        assertEquals("Lua", result.getContent().get(1).name());
        verify(repository, times(1)).findByStatus(eq(AdoptionStatus.DISPONIVEL), Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com filtro de espécie gatos")
    void testFindAll_ShouldReturnAPageOfCats() {
        Page<Animal> page = new PageImpl<>(List.of(createAnimal("2", "Lua", AnimalType.GATO, AnimalSex.FEMEA)));
        when(repository.findByStatusAndType(eq(AdoptionStatus.DISPONIVEL), eq(AnimalType.GATO), Mockito.any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalResponseDto> result = service.findAll(pageable, "Disponivel", "Gato");

        assertEquals(1, result.getTotalElements());
        assertEquals("Lua", result.getContent().get(0).name());
        verify(repository, times(1)).findByStatusAndType(eq(AdoptionStatus.DISPONIVEL), eq(AnimalType.GATO), Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de animais com filtro de status adotado")
    void testFindAll_ShouldReturnAPageOfAdoptedAnimals() {
        Animal dogEntity = createAnimal("1", "Rex", AnimalType.CACHORRO, AnimalSex.MACHO);
        Animal catEntity = createAnimal("2", "Lua", AnimalType.GATO, AnimalSex.FEMEA);
        dogEntity.setStatus(AdoptionStatus.ADOTADO);
        catEntity.setStatus(AdoptionStatus.ADOTADO);
        Page<Animal> page = new PageImpl<>(List.of(dogEntity, catEntity));
        when(repository.findByStatus(eq(AdoptionStatus.ADOTADO), Mockito.any(Pageable.class))).thenReturn(page);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalResponseDto> result = service.findAll(pageable, "Adotado", null);

        assertEquals(2, result.getTotalElements());
        assertEquals("Rex", result.getContent().get(0).name());
        assertEquals("Lua", result.getContent().get(1).name());
        assertEquals(AdoptionStatus.ADOTADO, result.getContent().get(0).status());
        assertEquals(AdoptionStatus.ADOTADO, result.getContent().get(1).status());
        verify(repository, times(1)).findByStatus(eq(AdoptionStatus.ADOTADO), Mockito.any(Pageable.class));
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

    Animal createAnimal(String id, String name, AnimalType type, AnimalSex sex) {
        return Animal.builder()
                .id(id)
                .name(name)
                .dob(LocalDate.of(2025, 10, 10))
                .weight(1200)
                .type(type)
                .sex(sex)
                .size(AnimalSize.PEQUENO)
                .registrationDate(LocalDateTime.of(2025, 10, 10, 12, 00, 00))
                .status(AdoptionStatus.DISPONIVEL)
                .description("Animal docil")
                .build();
    }
}