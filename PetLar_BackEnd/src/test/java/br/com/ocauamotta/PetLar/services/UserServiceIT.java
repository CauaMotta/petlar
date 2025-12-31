package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.User.*;
import br.com.ocauamotta.PetLar.exceptions.User.DuplicateEmailException;
import br.com.ocauamotta.PetLar.exceptions.User.SamePasswordException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceIT {

    @Autowired
    private UserService service;

    @Autowired
    private IUserRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("Users");
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso.")
    void shouldSaveUser() {
        UserRequestDto dto = new UserRequestDto(
                "user@teste.com",
                "123456",
                "Teste"
        );

        UserResponseDto response = service.save(dto);

        assertNotNull(response.id());
        assertEquals("user@teste.com", response.email());
        assertEquals("Teste", response.name());

        User saved = repository.findById(response.id()).orElseThrow();
        assertNotEquals("123456", saved.getPassword());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve impedir cadastro com email duplicado")
    void shouldThrowDuplicateEmailException() {
        service.save(new UserRequestDto(
                "user@teste.com",
                "123456",
                "Teste"
        ));

        UserRequestDto dto = new UserRequestDto(
                "user@teste.com",
                "abcdef",
                "Outro"
        );

        assertThrows(DuplicateEmailException.class,
                () -> service.save(dto));
    }

    @Test
    @DisplayName("Deve atualizar usuário sem trocar email")
    void shouldUpdateUserWithoutNewToken() {
        UserResponseDto created = service.save(
                new UserRequestDto("user@teste.com", "123456", "Teste")
        );

        User user = repository.findById(created.id()).orElseThrow();

        UserUpdateRequestDto dto =
                new UserUpdateRequestDto("user@teste.com", "Novo Nome");

        UserUpdateResponseDto response = service.update(dto, user);

        assertNull(response.newToken());
        assertEquals("Novo Nome", response.userResponseDto().name());
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void shouldChangePassword() {
        UserResponseDto created = service.save(
                new UserRequestDto("user@teste.com", "123456", "Teste")
        );

        User user = repository.findById(created.id()).orElseThrow();

        UserChangePasswordDto dto =
                new UserChangePasswordDto("novaSenha123");

        UserResponseDto response =
                service.changePassword(dto, user);

        User updated = repository.findById(response.id()).orElseThrow();

        assertNotEquals("novaSenha123", updated.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção ao repetir senha")
    void shouldThrowSamePasswordException() {
        UserResponseDto created = service.save(
                new UserRequestDto("user@teste.com", "123456", "Teste")
        );

        User user = repository.findById(created.id()).orElseThrow();

        UserChangePasswordDto dto =
                new UserChangePasswordDto("123456");

        assertThrows(SamePasswordException.class,
                () -> service.changePassword(dto, user));
    }

    @Test
    @DisplayName("Deve realizar exclusão lógica")
    void shouldDeleteUserLogically() {
        UserResponseDto created = service.save(
                new UserRequestDto("user@teste.com", "123456", "Teste")
        );

        User user = repository.findById(created.id()).orElseThrow();

        service.delete(user);

        User deleted = repository.findById(user.getId()).orElseThrow();
        assertNotNull(deleted.getDeletedAt());
    }
}