package br.com.ocauamotta.PetLar.exceptions.Animal;

/**
 * Exceção personalizada lançada para indicar que o usuário
 * não é o autor do registro do animal.
 */
public class UserWhoIsNotTheOwnerOfTheAnimalException extends RuntimeException {
    public UserWhoIsNotTheOwnerOfTheAnimalException(String message) {
        super(message);
    }
}
