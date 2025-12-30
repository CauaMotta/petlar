package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.User.UserChangePasswordDto;
import br.com.ocauamotta.PetLar.dtos.User.UserRequestDto;
import br.com.ocauamotta.PetLar.dtos.User.UserUpdateRequestDto;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.services.TokenService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenService tokenService;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo completo: cadastro, login e consulta /me")
    void shouldRegisterLoginAndGetMe() throws Exception {
        // cadastro
        UserRequestDto register =
                new UserRequestDto("user@teste.com", "123456", "Teste");

        mvc.perform(post("/api/users/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        // login
        User user = repository.findAll().get(0);
        String token = tokenService.generateToken(user);

        mvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@teste.com"));
    }

    @Test
    @DisplayName("Deve atualizar usuário autenticado e retornar novo token.")
    void shouldUpdateUserAndReturnNewToken() throws Exception {
        User user = repository.save(User.builder()
                .email("user@teste.com")
                .password(encoder.encode("123456"))
                .name("Teste")
                .build());

        String token = tokenService.generateToken(user);

        UserUpdateRequestDto dto =
                new UserUpdateRequestDto("novo@teste.com", "Novo Nome");

        mvc.perform(put("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @DisplayName("Deve atualizar usuário autenticado sem retornar novo token.")
    void shouldUpdateUserAndNotReturnNewToken() throws Exception {
        User user = repository.save(User.builder()
                .email("user@teste.com")
                .password(encoder.encode("123456"))
                .name("Teste")
                .build());

        String token = tokenService.generateToken(user);

        UserUpdateRequestDto dto =
                new UserUpdateRequestDto("user@teste.com", "Novo Nome");

        mvc.perform(put("/api/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Authorization"));
    }

    @Test
    @DisplayName("Deve atualizar a senha do usuário autenticado.")
    void shouldChangePasswordSuccessfully() throws Exception {
        User user = repository.save(User.builder()
                .email("user@teste.com")
                .password(encoder.encode("123456"))
                .name("Teste")
                .build());

        String token = tokenService.generateToken(user);

        UserChangePasswordDto dto =
                new UserChangePasswordDto("novaSenha");

        mvc.perform(put("/api/users/me/changePassword")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve deletar logicamente o usuário autenticado.")
    void shouldDeleteUserLogically() throws Exception {
        User user = repository.save(User.builder()
                .email("user@teste.com")
                .password(encoder.encode("123456"))
                .name("Teste")
                .build());

        String token = tokenService.generateToken(user);

        mvc.perform(delete("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}