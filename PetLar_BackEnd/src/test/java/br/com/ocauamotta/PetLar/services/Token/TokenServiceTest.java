package br.com.ocauamotta.PetLar.services.Token;

import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService service;

    @Test
    @DisplayName("Deve gerar um token válido.")
    void testShouldGenerateValidToken() {
        User user = createUser();

        String token = service.generateToken(user);

        assertNotNull(token);

        String subject = service.getSubject(token);
        assertEquals("user@teste.com", subject);
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