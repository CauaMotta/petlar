package br.com.ocauamotta.PetLar.repository;

import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IAnimalRepository<E extends Animal> extends MongoRepository<E, String> {
    Page<E> findByStatus(AdoptionStatus status, Pageable pageable);
}
