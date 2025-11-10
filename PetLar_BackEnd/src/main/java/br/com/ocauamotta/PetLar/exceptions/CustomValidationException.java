package br.com.ocauamotta.PetLar.exceptions;

import java.util.Map;

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
