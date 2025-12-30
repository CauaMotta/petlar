package br.com.ocauamotta.PetLar.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Adoptions")
public class Adoption {
    @Id
    private String id;

    private String animalId;
    private String adopterId;
    private String reason;

    private String createdAt;
    private String updatedAt;
}