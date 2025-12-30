package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.exceptions.DuplicateEmailException;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.exceptions.CustomValidationException;
import br.com.ocauamotta.PetLar.exceptions.SamePasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
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
     * Manipula exceções de credenciais inválidas {@code BadCredentialsException}.
     * Esta exceção é lançada pelo {@code AuthenticationManager} quando a combinação
     * de e-mail e senha fornecida pelo usuário não é valida.
     * Mapeia a exceção para o status HTTP 401 UNAUTHORIZED.
     *
     * @param ex A exceção {@code BadCredentialsException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 401 e o corpo {@code ErrorResponse},
     * contendo uma mensagem genérica de erro de login.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.UNAUTHORIZED.value(),
                "Email ou senha incorretos."
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Manipula exceções de e-mail duplicado {@code DuplicateEmailException}.
     * Esta exceção é lançada pela camada de serviço/validação para indicar que o
     * registro falhou devido a um e-mail já existente no sistema.
     * Mapeia a exceção para o status HTTP 409 CONFLICT.
     *
     * @param ex A exceção {@code DuplicateEmailException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 409 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Manipula exceções de senha idêntica {@code SamePasswordException}.
     * Esta exceção é lançada pela camada de validação quando o usuário tenta
     * alterar sua senha para uma que é igual à sua senha atual.
     * Mapeia a exceção para o status HTTP 400 BAD REQUEST.
     *
     * @param ex A exceção {@code SamePasswordException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ErrorResponse> handleSamePasswordException(SamePasswordException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manipula exceções de {@code EntityNotFoundException}.
     * Mapeia a exceção para o status HTTP 404 NOT FOUND.
     *
     * @param ex A exceção {@code EntityNotFoundException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 404 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
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
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manipula exceções de falha de validação automáticas do Spring ({@code MethodArgumentNotValidException}).
     * Esta exceção é lançada quando um DTO anotado com {@code @Valid} falha na validação.
     * Retorna o status HTTP 400 BAD REQUEST com o mapa de erros de campo no corpo do {@code ErrorResponse}.
     *
     * @param ex A exceção {@code MethodArgumentNotValidException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo o mapa de erros.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        CustomValidationException customEx = new CustomValidationException(errors);

        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                customEx.getMessage(),
                customEx.getErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manipula exceções de desserialização JSON.
     * Mapeia a exceção para o status HTTP 400 BAD REQUEST.
     *
     * @param ex A exceção {@code HttpMessageNotReadableException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String customMessage = "Corpo da requisição inválido. Verifique o formato JSON e os tipos de dados.";

        if (ex.getRootCause() instanceof DateTimeParseException || ex.getRootCause() instanceof DateTimeException) {
            customMessage = "Formato de data inválido. Formato esperado: 'yyyy-MM-dd'";
        }

        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                customMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manipula exceções de desserialização JSON.
     * Mapeia a exceção para o status HTTP 400 BAD REQUEST.
     *
     * @param ex A exceção {@code HttpMessageNotReadableException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String customMessage = "Método não suportado.";

        if(ex.getMethod().equalsIgnoreCase("POST")) {
            customMessage = "Método POST não é suportado.";
        }

        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                customMessage
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Trata quaisquer exceções de {@code RuntimeException} que não foram tratadas
     * especificamente por outras exceptions.
     * Retorna um status HTTP 500 INTERNAL SERVER ERROR.
     *
     * @param ex A {@code RuntimeException} capturada.
     * @param request O contexto da requisição web para obter o path.
     * @return {@code ResponseEntity} contendo o status 500 e um {@code ErrorResponse} detalhado.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro no servidor."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
