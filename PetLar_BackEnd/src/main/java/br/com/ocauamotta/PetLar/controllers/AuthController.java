package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Auth.AuthRequestDto;
import br.com.ocauamotta.PetLar.dtos.Auth.TokenResponse;
import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST responsável por gerenciar as operações de autenticação da aplicação.
 */
@RestController
@RequestMapping("${api.prefix}/login")
@Tag(name = "Autenticação", description = "Endpoint para Login")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint para realizar o login de um usuário.
     * <p>
     * Recebe as credenciais (e-mail/username e senha) no corpo da requisição e delega
     * o processo de autenticação ao {@code AuthenticationManager}.
     *
     * @param dto O DTO contendo o e-mail (username) e a senha do usuário.
     * @return Um {@code ResponseEntity} contendo o {@code TokenResponse} com o token JWT.
     */
    @Operation(
            summary = "Login de usuário",
            description = "Autentica um usuário com e-mail e senha e retorna um Token JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login bem-sucedido e Token JWT retornado",
                            useReturnTypeSchema = true,
                            content = @Content(mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvZSBTY2hlbGwgUHJvZ3JhbW1lciIsImlhdCI6MTUxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6y"
                                                            }
                                                            """
                                            )
                                    })
                    ),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2025-12-11T12:00:00.123456-03:00",
                                                                "path": "/api/login",
                                                                "status": 401,
                                                                "error": "Unauthorized",
                                                                "message": "Email ou senha incorretos."
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
                                                                "path": "/api/login",
                                                                "status": 500,
                                                                "message": "Ocorreu um erro no servidor."
                                                            }
                                                            """
                                            )
                                    }))
            }
    )
    @SecurityRequirements({})
    @PostMapping
    public ResponseEntity<TokenResponse> executeLogin(@RequestBody @Valid AuthRequestDto dto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authManager.authenticate(authToken);

        String tokenJWT = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(tokenJWT));
    }
}
