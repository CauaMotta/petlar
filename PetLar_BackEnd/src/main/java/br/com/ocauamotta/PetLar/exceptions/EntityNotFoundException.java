package br.com.ocauamotta.PetLar.exceptions;

/**
 * Exceção personalizada lançada para indicar que uma entidade
 * não foi encontrada no sistema.
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
