package br.com.ocauamotta.PetLar.controllers.User;

import br.com.ocauamotta.PetLar.dtos.User.*;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService service;

    @Test
    @DisplayName("Deve cadastrar novo usuário com sucesso.")
    void shouldCreateUser() throws Exception {
        UserRequestDto dto =
                new UserRequestDto("user@teste.com", "123456", "Teste");

        UserResponseDto response =
                new UserResponseDto("1", "user@teste.com", "Teste", null, null, null);

        when(service.save(any())).thenReturn(response);

        mvc.perform(post("/api/users/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@teste.com"));
    }

    @Test
    @DisplayName("Deve retornar o usuário autenticado.")
    @WithMockUser
    void shouldReturnAuthenticatedUser() throws Exception {
        User user = mockUser();

        when(service.findUser(any()))
                .thenReturn(new UserResponseDto(
                        "1", "user@teste.com", "Teste", null, null, null));

        mvc.perform(get("/api/users/me")
                        .principal(() -> "user@teste.com")
                        .requestAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@teste.com"));
    }

    @Test
    @DisplayName("Deve atualizar usuário autenticado e gerar novo token.")
    @WithMockUser
    void shouldUpdateUserAndReturnTokenInHeader() throws Exception {
        UserUpdateRequestDto dto =
                new UserUpdateRequestDto("novo@teste.com", "Novo Nome");

        UserResponseDto response =
                new UserResponseDto("1", "novo@teste.com", "Novo Nome", null, null, null);

        when(service.update(any(), any()))
                .thenReturn(new UserUpdateResponseDto(response, "new-token"));

        mvc.perform(put("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer new-token"))
                .andExpect(jsonPath("$.email").value("novo@teste.com"));
    }

    @Test
    @DisplayName("Deve atualizar a senha do usuário autenticado.")
    @WithMockUser
    void shouldChangePassword() throws Exception {
        UserChangePasswordDto dto =
                new UserChangePasswordDto("novaSenha");

        when(service.changePassword(any(), any()))
                .thenReturn(new UserResponseDto(
                        "1", "user@teste.com", "Teste", null, null, null));

        mvc.perform(put("/api/users/me/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve deletar logicamente o usuário autenticado.")
    @WithMockUser
    void shouldDeleteUser() throws Exception {
        doNothing().when(service).delete(any());

        mvc.perform(delete("/api/users/me"))
                .andExpect(status().isNoContent());
    }

    User mockUser() {
        return User.builder()
                .id("1")
                .email("user@teste.com")
                .name("Teste")
                .password("encrypted")
                .build();
    }
}