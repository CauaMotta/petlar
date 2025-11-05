package br.com.ocauamotta.PetLar.service;

import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.mapper.OtherMapper;
import br.com.ocauamotta.PetLar.repository.IOtherRepository;
import br.com.ocauamotta.PetLar.service.generic.GenericAnimalService;
import org.springframework.stereotype.Service;

@Service
public class OtherService extends GenericAnimalService<Other, IOtherRepository> {

    public OtherService(IOtherRepository repository) {
        super(repository);
    }

    @Override
    protected AnimalDTO toDTO(Other entity) {
        return OtherMapper.toDTO(entity);
    }

    @Override
    protected Other createEntity(CreateAnimalDTO dto) {
        return OtherMapper.create(dto);
    }

    @Override
    protected void setType(Other entity) {
        entity.setType(AnimalType.OTHER);
    }
}
