package br.com.ocauamotta.PetLar.exceptions;

/**
 * Exceção personalizada lançada para indicar algum erro
 * na verificação de tokens
 */
public class InvalidTokenException extends TokenException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
