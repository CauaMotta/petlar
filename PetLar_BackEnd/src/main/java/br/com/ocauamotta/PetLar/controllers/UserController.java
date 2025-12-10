package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.User.UserRequestDto;
import br.com.ocauamotta.PetLar.dtos.User.UserResponseDto;
import br.com.ocauamotta.PetLar.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
