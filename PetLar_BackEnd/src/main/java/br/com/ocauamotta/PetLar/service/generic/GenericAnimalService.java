package br.com.ocauamotta.PetLar.service.generic;

import br.com.ocauamotta.PetLar.domain.Animal;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.repository.IAnimalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Optional;

public abstract class GenericAnimalService<E extends Animal, R extends IAnimalRepository<E>>
        implements IAnimalCrudService {

    protected final R repository;

    protected GenericAnimalService(R repository) {
        this.repository = repository;
    }

    protected abstract AnimalDTO toDTO(E entity);
    protected abstract E createEntity(CreateAnimalDTO dto);
    protected abstract void setType(E entity);

    public AnimalDTO save(CreateAnimalDTO dto) {
        E entity = createEntity(dto);
        entity.setRegistrationDate(LocalDate.now());
        entity.setStatus(AdoptionStatus.AVAILABLE);
        setType(entity);
        return toDTO(repository.insert(entity));
    }

    public AnimalDTO update(AnimalDTO dto) {
        E entity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com ID: " + dto.getId()));

        updateEntityFromDto(entity, dto);

        return toDTO(repository.save(entity));
    }

    public void delete(String id) {
        Optional<E> entity = repository.findById(id);
        entity.ifPresent(repository::delete);
    }

    public AnimalDTO findById(String id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com ID: " + id));
        return toDTO(entity);
    }

    public Page<AnimalDTO> findAll(Pageable pageable, String status) {
        if (status == null) {
            return repository.findAll(pageable).map(this::toDTO);
        }
        return repository.findByStatus(AdoptionStatus.fromLabel(status), pageable).map(this::toDTO);
    }

    private void updateEntityFromDto(E entity, AnimalDTO dto) {
        Field[] fields = dto.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object value = field.get(dto);

                if (value != null) {
                    Field entityField = getFieldRecursive(entity.getClass(), field.getName());
                    entityField.setAccessible(true);

                    if (entityField.getType().isEnum() && value instanceof String strValue) {
                        setEnumValue(entity, entityField, strValue);
                    } else {
                        entityField.set(entity, value);
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println("Ignorando campo: " + field.getName());
            }
        }
    }

    private void setEnumValue(E entity, Field entityField, String strValue) throws IllegalAccessException {
        try {
            Method fromLabelMethod = entityField.getType().getDeclaredMethod("fromLabel", String.class);
            Object enumValue = fromLabelMethod.invoke(null, strValue);
            entityField.set(entity, enumValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Object enumValue = Enum.valueOf((Class<Enum>) entityField.getType(), strValue.toUpperCase());
            entityField.set(entity, enumValue);
        }
    }

    private Field getFieldRecursive(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
