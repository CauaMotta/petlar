package br.com.ocauamotta.PetLar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.ocauamotta.PetLar.repository")
public class MongoConfig {

}