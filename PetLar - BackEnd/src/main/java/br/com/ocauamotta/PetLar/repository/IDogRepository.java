package br.com.ocauamotta.PetLar.repository;

import br.com.ocauamotta.PetLar.domain.Dog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDogRepository extends MongoRepository<Dog, String> {
}
