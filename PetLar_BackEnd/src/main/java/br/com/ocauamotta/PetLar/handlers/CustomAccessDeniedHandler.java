package br.com.ocauamotta.PetLar.handlers;

import br.com.ocauamotta.PetLar.dtos.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handler customizado para interceptar e tratar falhas de AUTORIZAÇÃO
 * no Spring Security (erros 403 Forbidden).
 * <p>
 * É acionado quando um usuário AUTENTICADO tenta acessar um recurso
 * para o qual não possui as permissões necessárias. O objetivo é garantir
 * que a resposta seja um JSON padronizado.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Trata a exceção de acesso negado (autorização).
     * <p>
     * Define a resposta HTTP com status 403 Forbidden e escreve o erro
     * em formato JSON padronizado.
     *
     * @param request A requisição HTTP.
     * @param response A resposta HTTP onde o erro será escrito.
     * @param accessDeniedException A exceção {@code AccessDeniedException} que ocorreu.
     * @throws IOException Se houver um erro de I/O ao escrever a resposta.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                "Você não tem permissão para acessar este recurso."
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}
