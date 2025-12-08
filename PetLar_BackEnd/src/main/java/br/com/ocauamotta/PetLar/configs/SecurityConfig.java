package br.com.ocauamotta.PetLar.configs;

import br.com.ocauamotta.PetLar.filters.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SecurityFilter securityFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    /**
     * Define a cadeia de filtros de segurança (Security Filter Chain) para a aplicação.
     *
     * <p>Esta configuração estabelece:
     * <ul>
     * <li>Desabilita o CSRF (Cross-Site Request Forgery).</li>
     * <li>Define a política de criação de sessão como {@code SessionCreationPolicy.STATELESS}.</li>
     * <li>Permite acesso público ao endpoint de login.</li>
     * <li>Exige autenticação para todas as outras requisições.</li>
     * <li>Adiciona o {@code SecurityFilter} personalizado antes do filtro padrão de autenticação de usuário e senha.</li>
     * </ul>
     *
     * @param http O objeto {@code HttpSecurity} para configurar a segurança web.
     * @return O {@code SecurityFilterChain} configurado.
     * @throws Exception Se houver erro na configuração de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String loginPath = apiPrefix + "/login";

        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(loginPath).permitAll();
                    req.anyRequest().authenticated();
                })
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
