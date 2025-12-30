package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de gerenciamento de animais.
 * Permite realizar operações de CRUD e filtragem de registros.
 */
@RestController
@RequestMapping(path = "${api.prefix}/animals")
@Tag(name = "Animais", description = "Endpoints para gerenciamento de animais")
public class AnimalController {

    @Autowired
    private AnimalService service;

    /**
     * Retorna uma lista paginada de animais, podendo ser filtrada por status e tipo.
     *
     * @param pageable Informações de paginação.
     * @param status   Status de adoção do animal.
     * @param type     Tipo de animal.
     * @return Um {@code ResponseEntity} contendo uma {@code Page} de {@code AnimalResponseDto}
     * correspondente aos critérios de filtro.
     */
    @Operation(
            summary = "Listar animais",
            description = "Retorna uma lista paginada de animais, sendo possível filtrar por status e tipo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de animais retornada com sucesso",
                            useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @GetMapping
    public ResponseEntity<Page<AnimalResponseDto>> findAll(Pageable pageable,
                                                           @Parameter(description = "Status do animal, por padrão é disponivel")
                                                           @RequestParam(required = false, defaultValue = "disponivel") String status,
                                                           @Parameter(description = "Tipo de animal, por exemplo cachorro, gato, etc.")
                                                           @RequestParam(required = false) String type) {
        return ResponseEntity.ok(service.findAll(pageable, status, type));
    }

    /**
     * Busca um animal pelo seu ID.
     *
     * @param id Identificador do animal.
     * @return Um {@code ResponseEntity} contendo o {@code AnimalResponseDto} com as informações do animal encontrado.
     */
    @Operation(
            summary = "Buscar animal por ID",
            description = "Retorna um animal de acordo com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 404,
                                                                "message": "Nenhum registro encontrado com ID - 123"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<AnimalResponseDto> findById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Cadastra um novo animal.
     *
     * @param dto {@code AnimalRequestDto} contendo os dados do novo animal.
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Um {@code ResponseEntity} contendo o {@code AnimalResponseDto} com as informações do animal cadastrado.
     */
    @Operation(
            summary = "Cadastrar novo animal",
            description = "Cria um novo registro de animal no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Animal cadastrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals",
                                                                "status": 400,
                                                                "message": "Houve um erro de validação em um ou mais campos.",
                                                                "errors": {
                                                                    "name": "O nome é obrigatório.",
                                                                    "weight": "O peso é obrigatório."
                                                                }
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @PostMapping
    public ResponseEntity<AnimalResponseDto> save(@RequestBody @Valid AnimalRequestDto dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto, user));
    }

    /**
     * Atualiza as informações de um animal existente.
     *
     * @param id Identificador do animal.
     * @param dto {@code AnimalRequestDto} contendo os dados a serem atualizados.
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Um {@code ResponseEntity} contendo o {@code AnimalResponseDto} com as informações do animal atualizado.
     */
    @Operation(
            summary = "Atualizar animal existente",
            description = "Atualiza as informações de um animal com base no ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AnimalResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 403,
                                                                "message": "Você não possui permissão para alterar este animal."
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 404,
                                                                "message": "Nenhum registro encontrado com ID - 123"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<AnimalResponseDto> update(@PathVariable(value = "id") String id,
                                                    @RequestBody @Valid AnimalRequestDto dto,
                                                    @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.update(id, dto, user));
    }

    /**
     * Exclui permanentemente um animal do sistema pelo seu ID.
     *
     * @param id Identificador do animal.
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Um {@code ResponseEntity} vazio indicando sucesso na exclusão.
     */
    @Operation(
            summary = "Excluir um animal",
            description = "Remove permanentemente um animal do banco de dados de acordo com o ID informado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Animal excluído com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 403,
                                                                "message": "Você não possui permissão para alterar este animal."
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 404,
                                                                "message": "Nenhum registro encontrado com ID - 123"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/animals/123",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id, @AuthenticationPrincipal User user) {
        service.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
