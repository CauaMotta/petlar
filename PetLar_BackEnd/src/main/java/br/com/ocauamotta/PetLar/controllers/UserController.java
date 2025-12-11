package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.User.*;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.UserService;
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
public class UserController {

    @Autowired
    private UserService service;

    /**
     * Endpoint para registrar um novo usuário no sistema.
     *
     * @param dto O {@code UserRequestDto} contendo as informações do novo usuário.
     * @return Uma {@code ResponseEntity} contendo o {@code UserResponseDto} do usuário criado.
     */
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
    @DeleteMapping(value = "/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user) {
        service.delete(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
