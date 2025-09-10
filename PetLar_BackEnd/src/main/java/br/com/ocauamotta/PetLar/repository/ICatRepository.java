package br.com.ocauamotta.PetLar.repository;

import br.com.ocauamotta.PetLar.domain.Cat;
import org.springframework.stereotype.Repository;

@Repository
public interface ICatRepository extends IAnimalRepository<Cat> {
}
