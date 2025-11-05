package br.com.ocauamotta.PetLar.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface IDogRepository extends IAnimalRepository<Dog> {
}
