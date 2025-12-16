package br.com.ocauamotta.PetLar.validations;

import br.com.ocauamotta.PetLar.exceptions.DuplicateEmailException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserExistsButDeletedValidationTest {

    @Mock
    private IUserRepository repository;

    @InjectMocks
    private UserExistsButDeletedValidation userExistsButDeletedValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o email não existir em usuários logicamente deletados.")
    void validate_NotShouldThrowException() {
        User user = createUser();
        when(repository.existsByEmailIgnoreCaseAndDeletedAtIsNotNull(user.getEmail()))
                .thenReturn(false);

        userExistsButDeletedValidation.validate(user);

        verify(repository, times(1))
                .existsByEmailIgnoreCaseAndDeletedAtIsNotNull("user@teste.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o email existir em um usuário logicamente deletado.")
    void validate_ShouldThrowException() {
        User user = createUser();
        when(repository.existsByEmailIgnoreCaseAndDeletedAtIsNotNull(user.getEmail()))
                .thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userExistsButDeletedValidation.validate(user));
        verify(repository, times(1))
                .existsByEmailIgnoreCaseAndDeletedAtIsNotNull("user@teste.com");
    }

    User createUser() {
        return User.builder()
                .id("1")
                .email("user@teste.com")
                .password("secretPassword")
                .name("Teste")
                .build();
    }
}