package br.com.ocauamotta.PetLar.configs;

import br.com.ocauamotta.PetLar.filters.SecurityFilter;
import br.com.ocauamotta.PetLar.handlers.CustomAccessDeniedHandler;
import br.com.ocauamotta.PetLar.handlers.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração principal para o Spring Security.
 * <p>
 * A anotação {@code @EnableWebSecurity} ativa a segurança web do Spring
 * e a anotação {@code @Configuration} garante que os métodos anotados com {@code @Bean}
 * sejam registrados como componentes gerenciados pelo Spring.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    /**
     * Define a cadeia de filtros de segurança (Security Filter Chain) para a aplicação.
     *
     * <p>Esta configuração estabelece:
     * <ul>
     * <li>Desabilita o CSRF (Cross-Site Request Forgery).</li>
     * <li>Define a política de criação de sessão como {@code SessionCreationPolicy.STATELESS}.</li>
     * <li>Permite acesso público ao endpoint de login e de cadastro.</li>
     * <li>Exige autenticação para todas as outras requisições.</li>
     * <li>Define handlers customizados {@code CustomAuthenticationEntryPoint} e {@code CustomAccessDeniedHandler}
     * para tratar falhas de autenticação 401 Unauthorized e autorização 403 Forbidden.</li>
     * <li>Adiciona o {@code SecurityFilter} personalizado para processar o token JWT antes do filtro padrão de autenticação.</li>
     * </ul>
     *
     * @param http O objeto {@code HttpSecurity} para configurar a segurança web.
     * @param securityFilter O filtro customizado para processamento do token JWT.
     * @param accessDeniedHandler O handler customizado para respostas 403 Forbidden.
     * @param entryPoint O handler customizado para respostas 401 Unauthorized.
     * @return O {@code SecurityFilterChain} configurado.
     * @throws Exception Se houver erro na configuração de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter,
                                                   CustomAccessDeniedHandler accessDeniedHandler,
                                                   CustomAuthenticationEntryPoint entryPoint) throws Exception {
        String loginPath = apiPrefix + "/login";
        String registerPath = apiPrefix + "/users/cadastrar";
        String apiDocs = "/v3/api-docs/**";
        String swaggerUi = "/swagger-ui/**";

        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(loginPath).permitAll();
                    req.requestMatchers(registerPath).permitAll();
                    req.requestMatchers(apiDocs).permitAll();
                    req.requestMatchers(swaggerUi).permitAll();
                    req.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Expõe o {@code AuthenticationManager} como um Bean gerenciado pelo Spring.
     * <p>
     * O {@code AuthenticationManager} é injetado no {@code AuthController}
     * para iniciar o processo de autenticação.
     *
     * @param configuration A configuração de autenticação do Spring Security.
     * @return O {@code AuthenticationManager} configurado.
     * @throws Exception Se o {@code AuthenticationManager} não puder ser obtido.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Define o codificador de senhas (Password Encoder) a ser utilizado.
     * <p>
     * O {@code BCryptPasswordEncoder} é o padrão recomendado e amplamente utilizado,
     * pois implementa um algoritmo de hash forte e com salt automático.
     *
     * @return Uma instância do {@code PasswordEncoder} ({@code BCryptPasswordEncoder}).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
