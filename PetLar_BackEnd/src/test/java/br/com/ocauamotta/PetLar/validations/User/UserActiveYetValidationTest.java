package br.com.ocauamotta.PetLar.validations.User;

import br.com.ocauamotta.PetLar.exceptions.User.UserInactiveException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserActiveYetValidationTest {

    @Mock
    private IUserRepository repository;

    @InjectMocks
    private UserActiveYetValidation userActiveYetValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o usuário está ativo no sistema.")
    void testValidate_NotShouldThrowException() {
        User user = createUser();
        when(repository.existsByIdAndDeletedAtIsNull("1")).thenReturn(true);

        userActiveYetValidation.validate(user);

        verify(repository, times(1)).existsByIdAndDeletedAtIsNull("1");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não está mais ativo no sistema.")
    void testValidate_ShouldThrowException() {
        User user = createUser();
        when(repository.existsByIdAndDeletedAtIsNull("1")).thenReturn(false);

        assertThrows(UserInactiveException.class, () -> userActiveYetValidation.validate(user));
        verify(repository, times(1)).existsByIdAndDeletedAtIsNull("1");
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