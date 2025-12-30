package br.com.ocauamotta.PetLar.exceptions;

/**
 * Exceção personalizada lançada para indicar que a operação de persistência
 * ou registro falhou devido à tentativa de salvar um registro com um
 * endereço de e-mail que já existe no sistema.
 */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
