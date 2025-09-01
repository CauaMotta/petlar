package br.com.ocauamotta.PetLar.service;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.DogMapper;
import br.com.ocauamotta.PetLar.repository.IDogRepository;
import br.com.ocauamotta.PetLar.service.generic.GenericAnimalService;
import org.springframework.stereotype.Service;

@Service
public class DogService extends GenericAnimalService<Dog, IDogRepository> {

    public DogService(IDogRepository repository) {
        super(repository);
    }

    @Override
    protected AnimalDTO toDTO(Dog entity) {
        return DogMapper.toDTO(entity);
    }

    @Override
    protected Dog toEntity(AnimalDTO dto) {
        return DogMapper.toEntity(dto);
    }

    @Override
    protected Dog createEntity(CreateAnimalDTO dto) {
        return DogMapper.create(dto);
    }

    @Override
    protected void setType(Dog entity) {
        entity.setType(AnimalType.DOG);
    }
}
