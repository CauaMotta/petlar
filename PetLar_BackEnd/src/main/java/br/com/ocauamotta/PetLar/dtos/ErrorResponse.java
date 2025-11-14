package br.com.ocauamotta.PetLar.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * DTO padronizado para representar respostas de erro da API.
 * <p>
 * O campo {@code errors} é incluído no JSON de saída apenas se não for nulo
 * sendo ideal para retornar erros de validação com detalhes de campo.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Schema(description = "DTO padronizado para respostas de erro da API")
public class ErrorResponse {
    @Schema(description = "Data e hora do erro no fuso horário 'America/Sao_Paulo'",
            example = "2025-11-13T11:32:43.123456-03:00")
    private ZonedDateTime timestamp;
    @Schema(description = "Caminho da requisição que causou o erro",
            example = "/api/animals")
    private String path;
    @Schema(description = "O código de status HTTP do erro", example = "400")
    private Integer status;
    @Schema(description = "A mensagem principal do erro", example = "Parâmetros de entrada inválidos")
    private String message;
    @Schema(description = "Mapa de erros de validação detalhados por campo, presente apenas em erros 400",
            example = "{\"campoNome\": \"O campo nome é obrigatório\"}")
    private Map<String, String> errors;

    /**
     * Construtor completo para erros que incluem detalhes de validação.
     *
     * @param path O caminho da requisição.
     * @param status O código de status HTTP.
     * @param message A mensagem principal do erro.
     * @param errors O mapa de erros detalhados por campo.
     */
    public ErrorResponse(String path, Integer status, String message, Map<String, String> errors) {
        this.timestamp = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.path = path;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Construtor simplificado para erros que não possuem detalhes de validação.
     *
     * @param path O caminho da requisição.
     * @param status O código de status HTTP.
     * @param message A mensagem principal do erro.
     */
    public ErrorResponse(String path, Integer status, String message) {
        this.timestamp = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.path = path;
        this.status = status;
        this.message = message;
    }
}

