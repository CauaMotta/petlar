package br.com.ocauamotta.PetLar.exceptions.Auth;

import org.springframework.security.core.AuthenticationException;

/**
 * Exceção personalizada lançada para indicar algum erro
 * na geração de tokens
 * <p>
 * Estende {@code AuthenticationException} para garantir que seja tratada pelo
 * fluxo de segurança do Spring.
 */
public class TokenException extends AuthenticationException {
    public TokenException(String message) {
        super(message);
    }
}
