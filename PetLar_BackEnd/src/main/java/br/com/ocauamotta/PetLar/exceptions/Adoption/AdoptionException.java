package br.com.ocauamotta.PetLar.exceptions.Adoption;

/**
 * Classe base para todas as exceções de regra de negócio relacionadas ao
 * processo de adoção no sistema PetLar.
 */
public class AdoptionException extends RuntimeException {
    public AdoptionException(String message) {
        super(message);
    }
}
