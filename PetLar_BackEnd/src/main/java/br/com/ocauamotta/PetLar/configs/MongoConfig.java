package br.com.ocauamotta.PetLar.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuração de inicialização do Spring Data MongoDB.
 * Habilita o scanning de interfaces que estendem {@code MongoRepository}
 * no pacote de repositórios da aplicação.
 */
@Configuration
@EnableMongoRepositories(basePackages = "br.com.ocauamotta.PetLar.repositories")
public class MongoConfig {
}