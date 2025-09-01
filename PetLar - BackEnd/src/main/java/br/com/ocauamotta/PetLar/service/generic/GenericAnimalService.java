package br.com.ocauamotta.PetLar.service.generic;

import br.com.ocauamotta.PetLar.domain.Animal;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.repository.IAnimalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public abstract class GenericAnimalService<E extends Animal, R extends IAnimalRepository<E>>
        implements IAnimalCrudService {

    protected final R repository;

    protected GenericAnimalService(R repository) {
        this.repository = repository;
    }

    protected abstract AnimalDTO toDTO(E entity);
    protected abstract E toEntity(AnimalDTO dto);
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
        E entity = toEntity(dto);
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
}
