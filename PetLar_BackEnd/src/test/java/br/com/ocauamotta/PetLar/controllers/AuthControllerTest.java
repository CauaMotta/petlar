package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.Auth.AuthRequestDto;
import br.com.ocauamotta.PetLar.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthenticationManager authManager;

    @MockitoBean
    private TokenService tokenService;

    @Test
    @DisplayName("Deve autenticar usuário e retornar token JWT")
    void shouldAuthenticateAndReturnToken() throws Exception {
        AuthRequestDto dto =
                new AuthRequestDto("user@teste.com", "123456");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        mockUser(), null, null
                );

        when(authManager.authenticate(any()))
                .thenReturn(authentication);

        when(tokenService.generateToken(any()))
                .thenReturn("jwt-token");

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    private Object mockUser() {
        return br.com.ocauamotta.PetLar.models.User.builder()
                .id("1")
                .email("user@teste.com")
                .password("encrypted")
                .name("Teste")
                .build();
    }
}