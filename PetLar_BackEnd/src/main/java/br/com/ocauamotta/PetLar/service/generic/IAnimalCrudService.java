package br.com.ocauamotta.PetLar.service.generic;

import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAnimalCrudService {
    Page<AnimalDTO> findAll(Pageable pageable, String status);
    AnimalDTO findById(String id);
    AnimalDTO save(CreateAnimalDTO dto);
    AnimalDTO update(AnimalDTO dto);
    void delete(String id);
}
