package br.com.ocauamotta.PetLar.validations;

import br.com.ocauamotta.PetLar.exceptions.SamePasswordException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SamePasswordValidationTest {

    @Mock
    private IUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SamePasswordValidation samePasswordValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando as senhas são diferentes")
    void testValidate_NotShouldThrowException() {
        User storedUser = createUser();
        when(repository.findById("1")).thenReturn(Optional.of(storedUser));
        when(passwordEncoder.matches("123456789", storedUser.getPassword()))
                .thenReturn(false);

        User user = createUser();
        user.setPassword("123456789");
        samePasswordValidation.validate(user);

        verify(repository, times(1)).findById("1");
        verify(passwordEncoder, times(1))
                .matches("123456789", "secretPassword");
    }

    @Test
    @DisplayName("Deve lançar exceção quando as senhas são iguais")
    void testValidate_ShouldThrowException() {
        User user = createUser();
        when(repository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secretPassword", user.getPassword()))
                .thenReturn(true);

        assertThrows(SamePasswordException.class, () -> samePasswordValidation.validate(user));
        verify(repository, times(1)).findById("1");
        verify(passwordEncoder, times(1))
                .matches("secretPassword", "secretPassword");
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
