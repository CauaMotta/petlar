package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mappers.AnimalMapper;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AnimalService {

    @Autowired
    private IAnimalRepository repository;

    public Page<AnimalResponseDto> findAll(Pageable pageable, String status, String type) {
        if (type == null || type.isBlank()) {
            return repository.findByStatus(AdoptionStatus.fromString(status), pageable).map(AnimalMapper::toDTO);
        }
        return repository.findByStatusAndType(AdoptionStatus.fromString(status), AnimalType.fromString(type), pageable).map(AnimalMapper::toDTO);
    }

    public AnimalResponseDto findById(String id) {
        Animal entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada com ID - " + id));
        return AnimalMapper.toDTO(entity);
    }

    public AnimalResponseDto save(AnimalRequestDto dto) {
        Animal entity = AnimalMapper.createEntity(dto);
        entity.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        entity.setStatus(AdoptionStatus.DISPONIVEL);
        return AnimalMapper.toDTO(repository.insert(entity));
    }

    public AnimalResponseDto update(String id, AnimalRequestDto dto) {
        Animal entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada com ID - " + id));
        Boolean updated = updateAnimalFields(dto, entity);
        if (!updated) {
            throw new IllegalArgumentException("Nenhum campo para atualização foi informado.");
        }
        return AnimalMapper.toDTO(repository.save(entity));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private Boolean updateAnimalFields(AnimalRequestDto dto, Animal entity) {
        Boolean updated = false;

        for ( Field dtoField : dto.getClass().getDeclaredFields() ) {
            dtoField.setAccessible(true);

            if (dtoField.getName().equalsIgnoreCase("id")) {
                continue;
            }

            try {
                Object newValue = dtoField.get(dto);
                if (newValue == null) continue;

                Field entityField;
                try {
                    entityField = entity.getClass().getDeclaredField(dtoField.getName());
                } catch (NoSuchFieldException ex) {
                    continue;
                }

                if(Modifier.isFinal(entityField.getModifiers())) {
                    continue;
                }

                entityField.setAccessible(true);
                entityField.set(entity, newValue);
                updated = true;
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Erro ao tentar atualizar o campo " + dtoField.getName());
            }
        }

        return updated;
    }
}
