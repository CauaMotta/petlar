package br.com.ocauamotta.PetLar.exceptions.Adoption;

/**
 * Exceção lançada quando uma solicitação de adoção é feita para um animal
 * que não possui o status {@code DISPONIVEL}.
 */
public class AnimalNotAvailableException extends RuntimeException {
    public AnimalNotAvailableException(String message) {
        super(message);
    }
}
