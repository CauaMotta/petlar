package br.com.ocauamotta.PetLar.validations;

import br.com.ocauamotta.PetLar.models.User;

/**
 * Interface que define o contrato para todas as classes de validação de negócios
 * relacionadas à entidade {@code User}.
 * <p>
 * Este padrão permite a injeção de múltiplas regras de validação em uma lista para serem executadas sequencialmente.
 */
public interface IValidation {
    void validate(User user);
}