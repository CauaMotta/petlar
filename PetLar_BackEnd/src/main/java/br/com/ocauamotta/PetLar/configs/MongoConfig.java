package br.com.ocauamotta.PetLar.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.ocauamotta.PetLar.repositories")
public class MongoConfig {
}