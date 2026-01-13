package br.com.ocauamotta.PetLar.services.User;

import br.com.ocauamotta.PetLar.dtos.User.*;
import br.com.ocauamotta.PetLar.exceptions.User.DuplicateEmailException;
import br.com.ocauamotta.PetLar.exceptions.User.SamePasswordException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.services.TokenService;
import br.com.ocauamotta.PetLar.services.UserService;
import br.com.ocauamotta.PetLar.validations.User.SamePasswordUserValidation;
import br.com.ocauamotta.PetLar.validations.User.ExistsButDeletedUserValidation;
import br.com.ocauamotta.PetLar.validations.User.ExistsUserValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private ExistsUserValidation userExistsValidation;

    @Mock
    private ExistsButDeletedUserValidation userExistsButDeletedValidation;

    @Mock
    private SamePasswordUserValidation samePasswordValidation;
    
    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("Deve salvar um usuário com sucesso.")
    void testSave_ShouldSaveAnUser() {
        UserRequestDto dto = new UserRequestDto("user@teste.com", "123456", "Teste");

        when(encoder.encode(dto.password())).thenReturn("encryptedPassword");
        when(repository.insert(any(User.class))).thenAnswer(returnsFirstArg());

        UserResponseDto savedUser = service.save(dto);

        assertNotNull(savedUser);
        assertEquals("user@teste.com", savedUser.email());
        assertEquals("Teste", savedUser.name());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).insert(userCaptor.capture());

        User savedEntity = userCaptor.getValue();

        assertEquals("user@teste.com", savedEntity.getEmail());
        assertEquals("Teste", savedEntity.getName());
        assertEquals("encryptedPassword", savedEntity.getPassword());
        assertNotNull(savedEntity.getCreatedAt());
        assertNotNull(savedEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando já existir o email cadastrado.")
    void testSave_ShouldThrowDuplicateEmailException() {
        UserRequestDto dto = new UserRequestDto("user@teste.com", "123456", "Teste");

        doThrow(DuplicateEmailException.class).when(userExistsValidation)
                .validate(any(User.class));

        assertThrows(DuplicateEmailException.class, () -> service.save(dto));
        verify(userExistsValidation, times(1)).validate(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar um usuário sem alterar o email e sem gerar um novo token.")
    void testUpdate_ShouldReturnAnUpdatedUser() {
        User user = createUser();
        String time = user.getUpdatedAt();
        UserUpdateRequestDto dto = new UserUpdateRequestDto("user@teste.com", "Teste da Silva");

        when(repository.save(any(User.class))).thenAnswer(returnsFirstArg());

        UserUpdateResponseDto userUpdateResponseDto = service.update(dto, user);

        assertNotNull(userUpdateResponseDto);
        assertNull(userUpdateResponseDto.newToken());

        UserResponseDto userResponseDto = userUpdateResponseDto.userResponseDto();
        assertNotNull(userUpdateResponseDto.userResponseDto());
        assertEquals("Teste da Silva", userResponseDto.name());
        assertEquals("user@teste.com", userResponseDto.email());
        assertNotEquals(time, userResponseDto.updatedAt());
    }

    @Test
    @DisplayName("Deve atualizar um usuário com email diferente e gerar um novo token.")
    void testUpdate_ShouldReturnAnUpdatedUserWithToken() {
        User user = createUser();
        String time = user.getUpdatedAt();
        UserUpdateRequestDto dto = new UserUpdateRequestDto("novo.email@teste.com", "Teste da Silva");

        when(repository.save(any(User.class))).thenAnswer(returnsFirstArg());
        when(tokenService.generateToken(any(User.class))).thenReturn("novoToken");

        UserUpdateResponseDto userUpdateResponseDto = service.update(dto, user);

        assertNotNull(userUpdateResponseDto);
        assertNotNull(userUpdateResponseDto.newToken());

        UserResponseDto userResponseDto = userUpdateResponseDto.userResponseDto();
        assertNotNull(userUpdateResponseDto.userResponseDto());
        assertEquals("Teste da Silva", userResponseDto.name());
        assertEquals("novo.email@teste.com", userResponseDto.email());
        assertNotEquals(time, userResponseDto.updatedAt());
    }

    @Test
    @DisplayName("Deve lançar exceção ao trocar o email de um usuário por um email já existente.")
    void testUpdate_ShouldThrowDuplicateEmailException() {
        User user = createUser();
        UserUpdateRequestDto dto = new UserUpdateRequestDto("novo.email@teste.com", "Teste da Silva");

        doThrow(DuplicateEmailException.class).when(userExistsValidation)
                .validate(any(User.class));

        assertThrows(DuplicateEmailException.class, () -> service.update(dto, user));
        verify(userExistsValidation, times(1)).validate(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar a senha com sucesso.")
    void testChangePassword_ShouldUpdatePassword() {
        User user = createUser();
        String time = user.getUpdatedAt();
        UserChangePasswordDto dto = new UserChangePasswordDto("123456789");

        when(encoder.encode("123456789")).thenReturn("encryptedPassword");
        when(repository.save(any(User.class))).thenAnswer(returnsFirstArg());

        UserResponseDto updatedUser = service.changePassword(dto, user);

        assertNotNull(updatedUser);
        assertNotEquals(time, updatedUser.updatedAt());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());

        User savedEntity = userCaptor.getValue();

        assertNotEquals("123456789", savedEntity.getPassword());
        assertNotEquals("secretPassword", savedEntity.getPassword());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar a senha igual a antecessora.")
    void testChangePassword_ShouldThrowSamePasswordException() {
        User user = createUser();
        UserChangePasswordDto dto = new UserChangePasswordDto("secretPassword");

        doThrow(SamePasswordException.class).when(samePasswordValidation)
                .validate(any(User.class));

        assertThrows(SamePasswordException.class, () -> service.changePassword(dto, user));
        verify(samePasswordValidation, times(1)).validate(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar logicamente um usuário.")
    void testDelete_ShouldDeleteAnUser() {
        User user = createUser();
        assertNull(user.getDeletedAt());

        service.delete(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());

        User savedEntity = userCaptor.getValue();

        assertNotNull(savedEntity.getDeletedAt());
    }

    User createUser() {
        String time = ZonedDateTime
                .of(2025, 10, 15, 12, 5, 10, 15, ZoneId.of("America/Sao_Paulo"))
                .toString();

        return User.builder()
                .id("1")
                .email("user@teste.com")
                .password("secretPassword")
                .name("Teste")
                .createdAt(time)
                .updatedAt(time)
                .build();
    }
}