package br.com.ocauamotta.PetLar.service;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exception.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mapper.DogMapper;
import br.com.ocauamotta.PetLar.repository.IDogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DogService {

    private IDogRepository repository;

    public DogService(IDogRepository repository) {
        this.repository = repository;
    }

    public DogDTO save(CreateDogDTO createDogDTO) {
        Dog dog = DogMapper.create(createDogDTO);
        dog.setType(AnimalType.DOG);
        dog.setRegistrationDate(LocalDate.now());
        return DogMapper.toDTO(repository.insert(dog));
    }

    public DogDTO update(DogDTO dogDTO) {
        Dog dog = repository.save(DogMapper.toDog(dogDTO));
        return DogMapper.toDTO(dog);
    }

    public void delete(String id) {
        Optional<Dog> dog = repository.findById(id);
        dog.ifPresent(value -> repository.delete(value));
    }

    public DogDTO findById(String id) {
         Dog dog = repository.findById(id)
                 .orElseThrow(() -> new EntityNotFoundException("Dog não encontrado com ID: " + id));
         return DogMapper.toDTO(dog);
    }

    public Page<DogDTO> findAll(Pageable pageable, String status) {
        if (status == null) {
            return repository.findAll(pageable).map(DogMapper::toDTO);
        }
        return repository.findByStatus(AdoptionStatus.fromLabel(status), pageable).map(DogMapper::toDTO);
    }
}
