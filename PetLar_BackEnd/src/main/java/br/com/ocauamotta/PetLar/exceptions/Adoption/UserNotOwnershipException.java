package br.com.ocauamotta.PetLar.exceptions.Adoption;

/**
 * Exceção lançada quando um usuário tenta modificar ou cancelar uma
 * solicitação de adoção que não foi criada por ele.
 */
public class UserNotOwnershipException extends RuntimeException {
    public UserNotOwnershipException(String message) {
        super(message);
    }
}
