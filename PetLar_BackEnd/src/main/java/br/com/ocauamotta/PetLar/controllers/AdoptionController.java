package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
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
 * Controller que gerencia os endpoints relacionados ao processo de adoção de animais.
 * <p>
 * Todas as operações aqui exigem que o usuário esteja devidamente autenticado.
 */
@RestController
@RequestMapping(path = "${api.prefix}/adoptions")
@Tag(name = "Adoções", description = "Endpoints para gerenciamento e acompanhamento de processos de adoção.")
public class AdoptionController {

    @Autowired
    private AdoptionService service;

    /**
     * Cria uma nova solicitação de adoção para um animal.
     * <p>
     * O processo valida o corpo da requisição (ID do animal e justificativa) e identifica
     * automaticamente o usuário interessado através do contexto de segurança do Spring.
     *
     * @param dto  Objeto contendo o {@code animalId} e a justificativa {@code reason}.
     * @param user O usuário autenticado (adotante), injetado via {@code @AuthenticationPrincipal}.
     * @return Uma {@code ResponseEntity} com o status **201 CREATED** e os dados da adoção iniciada.
     */
    @Operation(
            summary = "Solicitar adoção",
            description = "Inicia um processo de adoção para um animal específico. O usuário autenticado é automaticamente registrado como o solicitante (adotante).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Solicitação de adoção criada com sucesso.",
                            useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Usuário não pode adotar o mesmo animal que cadastrou",
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/adoptions",
                                                                "status": 400,
                                                                "message": "Você não pode adotar o mesmo animal que cadastrou."
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Animal indisponivel para adoção",
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/adoptions",
                                                                "status": 400,
                                                                "message": "Este animal está com uma adoção em andamento ou já foi adotado."
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
                                                                "path": "/api/adoptions",
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
                                                                "path": "/api/adoptions",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @PostMapping
    public ResponseEntity<AdoptionResponseDto> initAdoption(@RequestBody @Valid AdoptionRequestDto dto,
                                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.initAdoption(dto, user));
    }

    /**
     * Lista todas as solicitações de adoção feitas pelo usuário autenticado.
     *
     * @param pageable Informações de paginação.
     * @param user O usuário autenticado obtido do contexto de segurança.
     * @return Uma {@code ResponseEntity} com a página de solicitações.
     */
    @GetMapping
    public ResponseEntity<Page<AdoptionResponseDto>> getMyAdoptionRequests(Pageable pageable,
                                                                           @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.getMyAdoptionRequests(pageable, user));
    }
}
