package br.com.ocauamotta.PetLar.repository;

import br.com.ocauamotta.PetLar.domain.Bird;
import org.springframework.stereotype.Repository;

@Repository
public interface IBirdRepository extends IAnimalRepository<Bird> {
}
