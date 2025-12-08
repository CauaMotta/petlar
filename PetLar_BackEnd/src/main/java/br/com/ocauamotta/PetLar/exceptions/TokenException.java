package br.com.ocauamotta.PetLar.exceptions;

/**
 * Exceção personalizada lançada para indicar algum erro
 * na geração de tokens
 */
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
