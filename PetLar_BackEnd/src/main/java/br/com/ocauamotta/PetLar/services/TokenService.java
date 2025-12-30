package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.exceptions.InvalidTokenException;
import br.com.ocauamotta.PetLar.exceptions.TokenException;
import br.com.ocauamotta.PetLar.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela geração de JWTs para a aplicação PetLar.
 * <p>
 * Utiliza a biblioteca {@code com.auth0.jwt} para criar e assinar os tokens,
 * garantindo a segurança das requisições. O segredo (secret) para a assinatura
 * do token é injetado através de uma propriedade de configuração.
 */
@Service
public class TokenService {

    /**
     * Chave secreta utilizada para assinar o JWT.
     * O valor é injetado a partir da propriedade {@code api.security.token.secret}
     * configurada no arquivo application.properties
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token (JWT) assinado para o usuário fornecido.
     * <p>
     * O token gerado possui as seguintes configurações:
     * <p>
     * <ul>
     * <li>Issuer: API PetLar</li>
     * <li>Subject: O username (email) do objeto {@code User}</li>
     * <li>Expires At: Data de expiração de uma hora a partir do momento da criação.</li>
     * </ul>
     *
     * @param user O objeto {@code User} para o qual o token será gerado.
     * @return Uma {@code String} representando o JWT assinado.
     * @throws TokenException Se ocorrer um erro durante o processo de criação do JWT.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API PetLar")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpires())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new TokenException("Erro ao gerar o token JWT");
        }
    }

    /**
     * Valida o token JWT e extrai o "Subject" (username/e-mail) contido nele.
     * <p>
     * Utiliza o algoritmo HMAC256 e o segredo configurado para verificar a
     * integridade e a validade do token (assinatura e expiração).
     *
     * @param tokenJWT O token JWT recebido na requisição.
     * @return A {@code String} do Subject (e-mail) contido no token.
     * @throws RuntimeException Se o token for inválido, tiver sido adulterado ou estiver expirado.
     */
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API PetLar")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new InvalidTokenException("Token JWT inválido ou expirado!");
        }
    }

    /**
     * Calcula o momento de expiração do token.
     * <p>
     * Define o tempo de expiração como uma hora após o momento atual,
     * utilizando o offset de fuso horário {@code -03:00} (Horário de Brasília).
     *
     * @return Um objeto {@code Instant} que representa o momento exato de expiração do token.
     */
    private Instant generateExpires() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
