package br.com.ocauamotta.PetLar.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeração que define os tipos de animais aceitos no sistema.
 * Cada valor do enum possui uma representação em string minúscula
 * (rótulo) para uso em requisições e persistência.
 */
@Schema(description = "Tipos de animal disponíveis")
public enum AnimalType {
    CACHORRO("cachorro"),
    GATO("gato"),
    PASSARO("passaro"),
    OUTRO("outro");

    private final String label;

    AnimalType(String label) {
        this.label = label;
    }

    /**
     * Retorna o rótulo em {@code String} do tipo de animal.
     *
     * @return O rótulo em {@code String}.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Converte uma string de rótulo para a constante {@code AnimalType} correspondente.
     * A comparação não diferencia maiúsculas e minúsculas.
     *
     * @param label A {@code String} do tipo de animal a ser convertida.
     * @return A constante {@code AnimalType} correspondente.
     * @throws IllegalArgumentException Se o rótulo fornecido não corresponder a nenhum tipo válido.
     */
    public static AnimalType fromString(String label) {
        for (AnimalType type : values()) {
            if(type.getLabel().equalsIgnoreCase(label)) return type;
        }
        throw new IllegalArgumentException("Tipo inválido: " + label);
    }
}
