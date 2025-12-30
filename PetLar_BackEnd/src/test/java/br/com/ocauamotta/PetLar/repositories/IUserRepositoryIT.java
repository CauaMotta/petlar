package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class IUserRepositoryIT {

    @Autowired
    private IUserRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve encontrar usuário ativo pelo email ignorando case")
    void shouldFindActiveUserByEmailIgnoreCase() {
        User user = createActiveUser("User@Teste.com");
        repository.save(user);

        Optional<UserDetails> result =
                repository.findByEmailIgnoreCaseAndDeletedAtIsNull("user@teste.com");

        assertTrue(result.isPresent());
        assertEquals("User@Teste.com", result.get().getUsername());
    }

    @Test
    @DisplayName("Não deve retornar usuário deletado logicamente")
    void shouldNotFindDeletedUser() {
        User user = createDeletedUser("user@teste.com");
        repository.save(user);

        Optional<UserDetails> result =
                repository.findByEmailIgnoreCaseAndDeletedAtIsNull("user@teste.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar true quando existir usuário ativo com email")
    void shouldReturnTrueWhenActiveUserExists() {
        repository.save(createActiveUser("user@teste.com"));

        Boolean exists =
                repository.existsByEmailIgnoreCaseAndDeletedAtIsNull("USER@TESTE.COM");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar false quando usuário ativo não existir")
    void shouldReturnFalseWhenActiveUserDoesNotExist() {
        Boolean exists =
                repository.existsByEmailIgnoreCaseAndDeletedAtIsNull("user@teste.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Deve retornar true quando existir usuário deletado com email")
    void shouldReturnTrueWhenDeletedUserExists() {
        repository.save(createDeletedUser("user@teste.com"));

        Boolean exists =
                repository.existsByEmailIgnoreCaseAndDeletedAtIsNotNull("user@teste.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar false quando não existir usuário deletado")
    void shouldReturnFalseWhenDeletedUserDoesNotExist() {
        repository.save(createActiveUser("user@teste.com"));

        Boolean exists =
                repository.existsByEmailIgnoreCaseAndDeletedAtIsNotNull("user@teste.com");

        assertFalse(exists);
    }

    User createActiveUser(String email) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15,
                        ZoneId.of("America/Sao_Paulo"))
                .toString();

        return User.builder()
                .email(email)
                .password("encryptedPassword")
                .name("Teste")
                .createdAt(time)
                .updatedAt(time)
                .deletedAt(null)
                .build();
    }

    User createDeletedUser(String email) {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15,
                        ZoneId.of("America/Sao_Paulo"))
                .toString();

        return User.builder()
                .email(email)
                .password("encryptedPassword")
                .name("Teste")
                .createdAt(time)
                .updatedAt(time)
                .deletedAt(time)
                .build();
    }
}