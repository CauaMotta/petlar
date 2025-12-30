package br.com.ocauamotta.PetLar.handlers;

import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler customizado para interceptar e tratar falhas de AUTENTICAÇÃO
 * no Spring Security (erros 401 Unauthorized).
 * <p>
 * É acionado quando um usuário não autenticado tenta acessar um recurso protegido.
 * O objetivo é garantir que a resposta seja um JSON padronizado.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Inicia o processo de tratamento de falha de autenticação.
     * <p>
     * Define a mensagem de erro específica para o caso de token ausente
     * {@code InsufficientAuthenticationException} e padroniza a resposta.
     *
     * @param request A requisição HTTP.
     * @param response A resposta HTTP onde o erro será escrito.
     * @param authException A exceção de autenticação que ocorreu.
     * @throws IOException Se houver um erro de I/O ao escrever a resposta.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String message = authException instanceof InsufficientAuthenticationException ?
                "Token não informado. Necessário autenticação para acessar este recurso."
                : authException.getMessage();

        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                message
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}
