package br.com.ocauamotta.PetLar.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeração que define os possíveis portes dos animais
 * aceitos no sistema.
 * Cada valor do enum possui uma representação em string minúscula
 * (rótulo) para uso em requisições e persistência.
 */
@Schema(description = "Porte de animais disponíveis")
public enum AnimalSize {
    PEQUENO("pequeno"),
    MEDIO("medio"),
    GRANDE("grande");

    private final String label;

    AnimalSize(String label) {
        this.label = label;
    }

    /**
     * Retorna o rótulo em {@code String} do porte do animal.
     *
     * @return O rótulo em {@code String}.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Converte uma string de rótulo para a constante {@code AnimalSize} correspondente.
     * A comparação não diferencia maiúsculas e minúsculas.
     *
     * @param label A {@code String} do porte do animal a ser convertida.
     * @return A constante {@code AnimalSize} correspondente.
     * @throws IllegalArgumentException Se o rótulo fornecido não corresponder a nenhum porte válido.
     */
    public static AnimalSize fromString(String label) {
        for (AnimalSize size : values()) {
            if(size.getLabel().equalsIgnoreCase(label)) return size;
        }
        throw new IllegalArgumentException("Porte inválido: " + label);
    }
}
