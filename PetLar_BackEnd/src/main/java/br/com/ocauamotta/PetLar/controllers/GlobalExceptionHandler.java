package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.exceptions.CustomValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe global de manipulação de exceções.
 * Captura exceções comuns da aplicação e as mapeia para
 * respostas HTTP padronizadas com o código de status apropriado.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manipula exceções de {@code EntityNotFoundException}.
     * Mapeia a exceção para o status HTTP 404 NOT FOUND.
     *
     * @param ex A exceção {@code EntityNotFoundException} lançada.
     * @return Uma {@code ResponseEntity} com o status 404 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Manipula exceções de {@code IllegalArgumentException}.
     * Mapeia a exceção para o status HTTP 400 BAD REQUEST.
     *
     * @param ex A exceção {@code IllegalArgumentException} lançada.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manipula exceções de falha de validação automáticas do Spring ({@code MethodArgumentNotValidException}).
     * Esta exceção é lançada quando um DTO anotado com {@code @Valid} falha na validação.
     * Mapeia os erros de campo para um map, cria uma {@code CustomValidationException} interna
     * e retorna o status HTTP 400 BAD REQUEST com o detalhe dos erros.
     *
     * @param ex A exceção {@code MethodArgumentNotValidException} lançada.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo o mapa de erros.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        CustomValidationException customEx = new CustomValidationException(errors);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                customEx.getMessage(),
                customEx.getErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
