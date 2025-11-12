package br.com.ocauamotta.PetLar.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

/**
 * DTO padronizado para representar respostas de erro da API.
 * <p>
 * O campo {@code errors} é incluído no JSON de saída apenas se não for nulo
 * sendo ideal para retornar erros de validação com detalhes de campo.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ErrorResponse {
    private Integer status;
    private String message;
    private Map<String, String> errors;

    /**
     * Construtor completo para erros que incluem detalhes de validação.
     *
     * @param status O código de status HTTP.
     * @param message A mensagem principal do erro.
     * @param errors O mapa de erros detalhados por campo.
     */
    public ErrorResponse(Integer status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Construtor simplificado para erros que não possuem detalhes de validação.
     *
     * @param status O código de status HTTP.
     * @param message A mensagem principal do erro.
     */
    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}

