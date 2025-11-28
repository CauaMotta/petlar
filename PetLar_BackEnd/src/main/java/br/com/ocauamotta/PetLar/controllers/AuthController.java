package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Auth.AuthRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    /**
     * Endpoint para realizar o login de um usuário.
     * <p>
     * Recebe as credenciais (e-mail/username e senha) no corpo da requisição e delega
     * o processo de autenticação ao {@code AuthenticationManager}.
     *
     * @param dto O DTO contendo o e-mail (username) e a senha do usuário.
     * @return {@code ResponseEntity}
     */
    @PostMapping
    public ResponseEntity executeLogin(@RequestBody @Valid AuthRequestDto dto) {
        var token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = authManager.authenticate(token);

        return ResponseEntity.ok().build();
    }
}
