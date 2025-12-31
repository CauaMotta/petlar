package br.com.ocauamotta.PetLar.exceptions.User;

/**
 * Exceção personalizada lançada quando uma operação é solicitada por um usuário
 * cujo registro consta como inativo no sistema (soft delete).
 */
public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String message) {
        super(message);
    }
}
