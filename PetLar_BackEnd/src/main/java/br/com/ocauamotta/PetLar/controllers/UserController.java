package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.dtos.User.*;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de gerenciamento de usuários.
 */
@RestController
@RequestMapping(path = "${api.prefix}/users")
@Tag(name = "Usuários", description = "Endpoints para registro, visualização e gerenciamento da própria conta (usuário autenticado).")
public class UserController {

    @Autowired
    private UserService service;

    /**
     * Endpoint para registrar um novo usuário no sistema.
     *
     * @param dto O {@code UserRequestDto} contendo as informações do novo usuário.
     * @return Uma {@code ResponseEntity} contendo o {@code UserResponseDto} do usuário criado.
     */
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário no sistema. Não requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.",
                            useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "409", description = "Email já cadastrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/cadastrar",
                                                                "status": 409,
                                                                "message": "Este email já está cadastrado."
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/cadastrar",
                                                                "status": 400,
                                                                "message": "Houve um erro de validação em um ou mais campos.",
                                                                "errors": {
                                                                    "email": "O campo email é obrigatório.",
                                                                    "password": "O campo senha é obrigatório.",
                                                                    "name": "O campo nome é obrigatório."
                                                                }
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/cadastrar",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @SecurityRequirements({})
    @PostMapping(value = "/cadastrar")
    public ResponseEntity<UserResponseDto> cadastrar(@RequestBody @Valid UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    /**
     * Busca os dados do usuário autenticado no momento.
     * <p>
     * O objeto {@code User} é injetado automaticamente pelo Spring Security
     * através da anotação {@code @AuthenticationPrincipal}.
     *
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Uma {@code ResponseEntity} com o {@code UserResponseDto} do usuário logado.
     */
    @Operation(
            summary = "Obter dados do usuário autenticado",
            description = "Retorna as informações do próprio usuário (requer autenticação).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso.",
                            useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 401,
                                                                "message": "Token JWT inválido ou expirado!"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @GetMapping(value = "/me")
    public ResponseEntity<UserResponseDto> findMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.findUser(user));
    }

    /**
     * Atualiza as informações do usuário autenticado.
     * <p>
     * Se o e-mail for alterado, um novo token JWT é gerado e retornado no cabeçalho
     * {@code Authorization} da resposta, obrigando o cliente a usar o novo token.
     *
     * @param dto O {@code UserUpdateRequestDto} com os dados de atualização.
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Uma {@code ResponseEntity} com o {@code UserResponseDto} atualizado.
     * O cabeçalho {@code Authorization} é incluído se o token for renovado.
     */
    @Operation(
            summary = "Atualizar dados do usuário autenticado",
            description = "Atualiza informações do usuário. Se o e-mail for alterado, um novo Token JWT é retornado no cabeçalho 'Authorization'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso.",
                            useReturnTypeSchema = true,
                            headers = @Header(
                                    name = "Authorization",
                                    description = "Novo Token JWT se o e-mail for alterado.",
                                    schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "400", description = "Dados fornecidos inválidos.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 400,
                                                                "message": "Houve um erro de validação em um ou mais campos.",
                                                                "errors": {
                                                                    "email": "O campo email é obrigatório.",
                                                                    "name": "O campo nome é obrigatório."
                                                                }
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "409", description = "Email já cadastrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 409,
                                                                "message": "Este email já está cadastrado."
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 401,
                                                                "message": "Token JWT inválido ou expirado!"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @PutMapping(value = "/me")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserUpdateRequestDto dto, @AuthenticationPrincipal User user) {
        UserUpdateResponseDto result = service.update(dto, user);

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();

        if (result.newToken() != null) {
            builder.header("Authorization", "Bearer " + result.newToken());
        }

        return builder.body(result.userResponseDto());
    }

    /**
     * Altera a senha do usuário autenticado.
     *
     * @param dto O {@code UserChangePasswordDto} contendo a nova senha bruta.
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Uma {@code ResponseEntity} com o {@code UserResponseDto} (sem a nova senha).
     */
    @Operation(
            summary = "Alterar senha",
            description = "Permite que o usuário autenticado defina uma nova senha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso.",
                            useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Senha fornecida inválida.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Senha fornecida inválida.",
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me/changePassword",
                                                                "status": 400,
                                                                "message": "Houve um erro de validação em um ou mais campos.",
                                                                "errors": {
                                                                    "password": "O campo senha é obrigatório."
                                                                }
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Senha igual às antecessoras.",
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me/changePassword",
                                                                "status": 400,
                                                                "message": "A senha tem que ser diferente das antecessoras."
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me/changePassword",
                                                                "status": 401,
                                                                "message": "Token JWT inválido ou expirado!"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me/changePassword",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @PutMapping(value = "/me/changePassword")
    public ResponseEntity<UserResponseDto> changePassword(@RequestBody @Valid UserChangePasswordDto dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.changePassword(dto, user));
    }

    /**
     * Realiza a exclusão lógica do usuário autenticado.
     * <p>
     * O registro é mantido no banco de dados, mas o campo {@code deletedAt} é preenchido.
     *
     * @param user O usuário autenticado, injetado pelo Spring Security.
     * @return Uma {@code ResponseEntity} vazia.
     */
    @Operation(
            summary = "Exclusão lógica do usuário",
            description = "Realiza a exclusão lógica da conta do usuário autenticado.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário excluído logicamente com sucesso."),
                    @ApiResponse(responseCode = "401", description = "Token JWT inválido ou expirado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 401,
                                                                "message": "Token JWT inválido ou expirado!"
                                                            }
                                                            """
                                            )
                                    })),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-11-10T12:00:00.123456-03:00",
                                                                "path": "/api/users/me",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @DeleteMapping(value = "/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user) {
        service.delete(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
