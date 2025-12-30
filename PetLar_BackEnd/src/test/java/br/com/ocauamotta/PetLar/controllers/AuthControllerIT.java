package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Auth.AuthRequestDto;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve autenticar usuário e retornar token JWT")
    void shouldLoginSuccessfully() throws Exception {
        User user = User.builder()
                .email("user@teste.com")
                .password(encoder.encode("123456"))
                .name("Teste")
                .build();

        repository.save(user);

        AuthRequestDto dto =
                new AuthRequestDto("user@teste.com", "123456");

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Deve retornar 401 ao informar credenciais inválidas")
    void shouldReturnUnauthorizedWhenInvalidCredentials() throws Exception {
        AuthRequestDto dto =
                new AuthRequestDto("user@teste.com", "senhaErrada");

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}