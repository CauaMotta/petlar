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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserExistsValidationTest {

    @Mock
    private IUserRepository repository;

    @InjectMocks
    private UserExistsValidation userExistsValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o email não existir.")
    void testValidate_NotShouldThrowException() {
        User user = createUser();
        when(repository.existsByEmailIgnoreCaseAndDeletedAtIsNull(user.getEmail()))
                .thenReturn(false);

        userExistsValidation.validate(user);

        verify(repository, times(1))
                .existsByEmailIgnoreCaseAndDeletedAtIsNull("user@teste.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o email existir.")
    void testValidate_ShouldThrowException() {
        User user = createUser();
        when(repository.existsByEmailIgnoreCaseAndDeletedAtIsNull(user.getEmail()))
                .thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userExistsValidation.validate(user));
        verify(repository, times(1))
                .existsByEmailIgnoreCaseAndDeletedAtIsNull("user@teste.com");
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