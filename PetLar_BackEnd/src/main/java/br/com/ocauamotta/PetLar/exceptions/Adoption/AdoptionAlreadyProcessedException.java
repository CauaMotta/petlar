package br.com.ocauamotta.PetLar.exceptions.Adoption;

/**
 * Exceção lançada quando ocorre uma tentativa de alteração em uma
 * solicitação de adoção que já saiu do estado {@code PENDENTE}.
 */
public class AdoptionAlreadyProcessedException extends RuntimeException {
    public AdoptionAlreadyProcessedException(String message) {
        super(message);
    }
}
