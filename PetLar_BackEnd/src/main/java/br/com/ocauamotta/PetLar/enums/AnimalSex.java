package br.com.ocauamotta.PetLar.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeração que define os sexos dos animais no sistema.
 * Cada valor do enum possui uma representação em string minúscula
 * (rótulo) para uso em requisições e persistência.
 */
@Schema(description = "Sexos disponíveis")
public enum AnimalSex {
    MACHO("macho"),
    FEMEA("femea");

    private final String label;

    AnimalSex(String label) {
        this.label = label;
    }

    /**
     * Retorna o rótulo em {@code String} do sexo do animal.
     *
     * @return O rótulo em {@code String}.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Converte uma string de rótulo para a constante {@code AnimalSex} correspondente.
     * A comparação não diferencia maiúsculas e minúsculas.
     *
     * @param label A {@code String} do sexo do animal a ser convertida.
     * @return A constante {@code AnimalSex} correspondente.
     * @throws IllegalArgumentException Se o rótulo fornecido não corresponder a nenhum sexo válido.
     */
    public static AnimalSex fromString(String label) {
        for (AnimalSex sex : values()) {
            if(sex.getLabel().equalsIgnoreCase(label)) return sex;
        }
        throw new IllegalArgumentException("Sexo inválido: " + label);
    }
}
