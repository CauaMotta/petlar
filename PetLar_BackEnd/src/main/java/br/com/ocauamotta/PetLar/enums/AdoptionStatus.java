package br.com.ocauamotta.PetLar.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeração que define o status atual de um animal em relação ao processo de adoção.
 * <p>
 * Cada valor do enum possui uma representação em string minúscula
 * (rótulo) para uso em requisições e persistência.
 */
@Schema(description = "Status de adoção disponíveis")
public enum AdoptionStatus {
    DISPONIVEL("disponivel"),
    PENDENTE("pendente"),
    CANCELADO("cancelado"),
    RECUSADO("recusado"),
    ADOTADO("adotado");

    private final String label;

    AdoptionStatus(String label) {
        this.label = label;
    }

    /**
     * Retorna o rótulo em {@code String} do status de adoção.
     *
     * @return O rótulo em {@code String}.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Converte uma string de rótulo para a constante {@code AdoptionStatus} correspondente.
     * A comparação não diferencia maiúsculas e minúsculas.
     *
     * @param label A {@code String} do status de adoção a ser convertida.
     * @return A constante {@code AdoptionStatus} correspondente.
     * @throws IllegalArgumentException Se o rótulo fornecido não corresponder a nenhum status válido.
     */
    public static AdoptionStatus fromString(String label) {
        for (AdoptionStatus status : values()) {
            if(status.getLabel().equalsIgnoreCase(label)) return status;
        }
        throw new IllegalArgumentException("Status inválido: " + label);
    }
}
