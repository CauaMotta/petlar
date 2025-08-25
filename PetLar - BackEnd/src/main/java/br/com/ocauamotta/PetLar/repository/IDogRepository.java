package br.com.ocauamotta.PetLar.repository;

import br.com.ocauamotta.PetLar.domain.Dog;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDogRepository extends MongoRepository<Dog, String> {

    Page<Dog> findByStatus(AdoptionStatus status, Pageable pageable);
}
