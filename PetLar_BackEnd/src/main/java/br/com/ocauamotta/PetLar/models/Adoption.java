package br.com.ocauamotta.PetLar.models;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidade de domínio que representa uma solicitação de adoção no sistema.
 * <p>
 * Esta classe é mapeada para a coleção {@code Adoptions} no MongoDB e armazena
 * o vínculo entre um {@code User} (adotante), o {@code User} autor do animal e um {@code Animal}, além do
 * status atual do processo e a justificativa fornecida.
 */
@Setter
@Getter
@Builder
@Document(collection = "Adoptions")
public class Adoption {
    @Id
    private String id;

    private AdoptionStatus status;
    private String animalId;
    private String animalOwnerId;
    private String adopterId;
    private String reason;

    private String createdAt;
    private String updatedAt;
}