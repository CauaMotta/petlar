package br.com.ocauamotta.PetLar.validations.Animal;

import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;

/**
 * Interface que define o contrato para todas as classes de validação de negócios
 * relacionadas à entidade {@code Animal}.
 * <p>
 * Este padrão permite a injeção de múltiplas regras de validação em uma lista para serem executadas sequencialmente.
 */
public interface IAnimalValidation {
    void validate(Animal animal, User user);
}