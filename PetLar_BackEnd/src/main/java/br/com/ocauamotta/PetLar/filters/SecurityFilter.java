package br.com.ocauamotta.PetLar.filters;

import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança personalizado responsável por processar o JWT presente
 * no cabeçalho de cada requisição.
 * <p>
 * Estende {@code OncePerRequestFilter} para garantir que o filtro seja executado
 * exatamente uma vez por requisição.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IUserRepository repository;

    /**
     * Lógica principal de filtragem.
     * <p>
     * Se um token JWT válido for encontrado no cabeçalho "Authorization":
     * <ol>
     * <li>Extrai o "Subject" (e-mail do usuário) do token.</li>
     * <li>Busca o {@code UserDetails} correspondente no repositório.</li>
     * <li>Cria um objeto de autenticação {@code UsernamePasswordAuthenticationToken}.</li>
     * <li>Define o objeto de autenticação no {@code SecurityContextHolder}, autenticando o usuário.</li>
     * </ol>
     *
     * @param request O objeto {@code HttpServletRequest}.
     * @param response O objeto {@code HttpServletResponse}.
     * @param filterChain A cadeia de filtros para continuar o processamento da requisição.
     * @throws ServletException Se ocorrer um erro durante o processamento do servlet.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenJWT = getToken(request);

        if (tokenJWT != null) {
            String subject = tokenService.getSubject(tokenJWT);
            UserDetails user = repository.findByEmail(subject);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token JWT do cabeçalho de autorização.
     * <p>
     * Espera o formato: {@code Authorization: Bearer <token>}.
     *
     * @param request O objeto {@code HttpServletRequest}.
     * @return O token JWT em formato {@code String}, ou {@code null} se o cabeçalho não estiver presente ou estiver no formato incorreto.
     */
    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
