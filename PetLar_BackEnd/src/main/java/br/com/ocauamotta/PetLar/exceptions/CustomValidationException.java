package br.com.ocauamotta.PetLar.exceptions;

import java.util.Map;

/**
 * Exceção personalizada lançada para indicar que um ou mais campos de entrada
 * falharam nas regras de validação.
 */
public class CustomValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors) {
        super("Erro de validação");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
