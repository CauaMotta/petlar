package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Auth.AuthRequestDto;
import br.com.ocauamotta.PetLar.dtos.Auth.TokenResponse;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.TokenService;
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
    @PostMapping
    public ResponseEntity<TokenResponse> executeLogin(@RequestBody @Valid AuthRequestDto dto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication auth = authManager.authenticate(authToken);

        String tokenJWT = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(tokenJWT));
    }
}
