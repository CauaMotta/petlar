package br.com.ocauamotta.PetLar.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa a entidade de um usuário no sistema, mapeado como um documento na coleção "Users" do MongoDB.
 */
@Document(collection = "Users")
public class User {

    @Id
    private String id;
    private String email;
    private String password;
}
