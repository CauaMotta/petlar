package br.com.ocauamotta.PetLar.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração do OpenAPI (Swagger).
 * Define metadados básicos sobre a API PetLar.
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PetLar Api")
                        .description("Está é a documentação da API Rest para o projeto PetLar")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Cauã Motta")
                                .email("ocauamotta@gmail.com")));
    }
}
