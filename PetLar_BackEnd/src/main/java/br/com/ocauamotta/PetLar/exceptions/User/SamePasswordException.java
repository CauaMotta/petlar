package br.com.ocauamotta.PetLar.exceptions.User;

/**
 * Exceção personalizada lançada para indicar que a operação de mudança de senha
 * foi rejeitada porque a nova senha fornecida é idêntica à senha
 * atualmente em uso pelo usuário.
 */
public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) {
        super(message);
    }
}
