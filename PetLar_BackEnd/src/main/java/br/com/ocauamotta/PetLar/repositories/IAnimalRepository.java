package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnimalRepository extends MongoRepository<Animal, String> {
    Page<Animal> findByStatus(AdoptionStatus adoptionStatus, Pageable pageable);
    Page<Animal> findByStatusAndType(AdoptionStatus adoptionStatus, AnimalType type, Pageable pageable);
}
