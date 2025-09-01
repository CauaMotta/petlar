package br.com.ocauamotta.PetLar.repository;

import br.com.ocauamotta.PetLar.domain.Dog;
import org.springframework.stereotype.Repository;

@Repository
public interface IDogRepository extends IAnimalRepository<Dog> {
}
