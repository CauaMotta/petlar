package br.com.ocauamotta.PetLar.service;

import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.BirdMapper;
import br.com.ocauamotta.PetLar.repository.IBirdRepository;
import br.com.ocauamotta.PetLar.service.generic.GenericAnimalService;
import org.springframework.stereotype.Service;

@Service
public class BirdService extends GenericAnimalService<Bird, IBirdRepository> {

    public BirdService(IBirdRepository repository) {
        super(repository);
    }

    @Override
    protected AnimalDTO toDTO(Bird entity) {
        return BirdMapper.toDTO(entity);
    }

    @Override
    protected Bird createEntity(CreateAnimalDTO dto) {
        return BirdMapper.create(dto);
    }

    @Override
    protected void setType(Bird entity) {
        entity.setType(AnimalType.BIRD);
    }
}
