package br.com.ocauamotta.PetLar.service;

import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.CatMapper;
import br.com.ocauamotta.PetLar.repository.ICatRepository;
import br.com.ocauamotta.PetLar.service.generic.GenericAnimalService;
import org.springframework.stereotype.Service;

@Service
public class CatService extends GenericAnimalService<Cat, ICatRepository> {

    public CatService(ICatRepository repository) {
        super(repository);
    }

    @Override
    protected AnimalDTO toDTO(Cat entity) {
        return CatMapper.toDTO(entity);
    }

    @Override
    protected Cat createEntity(CreateAnimalDTO dto) {
        return CatMapper.create(dto);
    }

    @Override
    protected void setType(Cat entity) {
        entity.setType(AnimalType.CAT);
    }
}
