package br.com.ocauamotta.PetLar.domain;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@SuperBuilder
@Document(collection = "Dogs")
public class Dog extends Animal{
}

