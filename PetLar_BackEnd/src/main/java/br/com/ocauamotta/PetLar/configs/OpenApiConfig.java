package br.com.ocauamotta.PetLar.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração do OpenAPI (Swagger).
 * Define metadados básicos sobre a API PetLar.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Insira o Token JWT para acessar os recursos protegidos da API.");

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .info(new Info()
                        .title("API Rest PetLar - Documentação Oficial")
                        .description(
                                "Esta documentação detalhada apresenta a totalidade dos recursos da API Rest PetLar, " +
                                        "desenvolvida para gerenciar dados e funcionalidades cruciais do sistema. " +
                                        "Os endpoints protegidos requerem autenticação via JWT, " +
                                        "garantindo a segurança e integridade das operações."
                        )
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Cauã Motta")
                                .email("ocauamotta@gmail.com")));
    }
}
