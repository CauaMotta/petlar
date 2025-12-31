package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.exceptions.*;
import br.com.ocauamotta.PetLar.exceptions.Adoption.*;
import br.com.ocauamotta.PetLar.exceptions.Animal.UserWhoIsNotTheOwnerOfTheAnimalException;
import br.com.ocauamotta.PetLar.exceptions.User.DuplicateEmailException;
import br.com.ocauamotta.PetLar.exceptions.User.SamePasswordException;
import br.com.ocauamotta.PetLar.exceptions.User.UserInactiveException;
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
     * Manipula exceções de operações realizadas por usuários inativos.
     * <p>
     * Quando uma ação tenta ser realizada envolvendo um usuário que sofreu *soft delete*, esta
     * exceção é capturada e retorna o status 400 BAD REQUEST, sinalizando
     * que a conta informada não é elegível para processamento.
     *
     * @param ex A exceção {@code UserInactiveException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo a mensagem detalhada.
     */
    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<ErrorResponse> handleUserInactiveException(
            UserInactiveException ex, WebRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Manipula exceções de animal não pertencente ao usuário autenticado {@code UserWhoIsNotTheOwnerOfTheAnimalException}.
     * Esta exceção é lançada pelo {@code AnimalOwnerUserValidation} quando o usuário autenticado
     * não é o autor do registro do animal.
     * Mapeia a exceção para o status HTTP 403 FORBIDDEN.
     *
     * @param ex A exceção {@code UserWhoIsNotTheOwnerOfTheAnimalException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 403 e o corpo {@code ErrorResponse}
     * contendo a mensagem detalhada.
     */
    @ExceptionHandler(UserWhoIsNotTheOwnerOfTheAnimalException.class)
    public ResponseEntity<ErrorResponse> handleUserWhoIsNotTheOwnerOfTheAnimalException(
            UserWhoIsNotTheOwnerOfTheAnimalException ex, WebRequest request) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    /**
     * Trata tentativas de um usuário adotar o próprio animal cadastrado.
     *
     * @param ex A exceção {@code TryAdoptionYourOwnPetException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo a mensagem detalhada.
     */
    @ExceptionHandler(TryAdoptionYourOwnPetException.class)
    public ResponseEntity<ErrorResponse> handleTryAdoptionYourOwnPetException(
            TryAdoptionYourOwnPetException ex, WebRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Trata tentativas de adotar animais que não estão com status 'DISPONIVEL'.
     *
     * @param ex A exceção {@code AnimalNotAvailableException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo a mensagem detalhada.
     */
    @ExceptionHandler(AnimalNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleAnimalNotAvailableException(
            AnimalNotAvailableException ex, WebRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Trata tentativas de modificar uma adoção que já foi finalizada (Aprovada/Rejeitada/Cancelada).
     *
     * @param ex A exceção {@code AdoptionAlreadyProcessedException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo a mensagem detalhada.
     */
    @ExceptionHandler(AdoptionAlreadyProcessedException.class)
    public ResponseEntity<ErrorResponse> handleAdoptionAlreadyProcessedException(
            AdoptionAlreadyProcessedException ex, WebRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Trata violações de propriedade, onde um usuário tenta agir sobre uma adoção de terceiro.
     *
     * @param ex A exceção {@code UserNotAdopterException} lançada.
     * @param request O contexto da requisição web para obter o path.
     * @return Uma {@code ResponseEntity} com o status 400 e o corpo {@code ErrorResponse}
     * contendo a mensagem detalhada.
     */
    @ExceptionHandler(UserNotOwnershipException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAdopterException(
            UserNotOwnershipException ex, WebRequest request) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

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
        return buildError(HttpStatus.UNAUTHORIZED, "Email ou senha incorretos.", request);
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
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
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
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
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
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
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
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
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

        return buildError(HttpStatus.BAD_REQUEST, customMessage, request);
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

        return buildError(HttpStatus.METHOD_NOT_ALLOWED, customMessage, request);
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
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro no servidor.", request);
    }

    /**
     * Método utilitário para construir a resposta de erro padronizada da API.
     *
     * @param status O status HTTP apropriado para o erro.
     * @param message A mensagem explicativa da exceção.
     * @param req A requisição web para extração do endpoint.
     * @return Uma {@code ResponseEntity} contendo o objeto {@code ErrorResponse}.
     */
    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, WebRequest req) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                req.getDescription(false).replace("uri=", ""),
                status.value(),
                message
        ));
    }
}
