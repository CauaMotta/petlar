package br.com.ocauamotta.PetLar.enums;

public enum AnimalType {
    CACHORRO("cachorro"),
    GATO("gato"),
    PASSARO("passaro"),
    OUTRO("outro");

    private final String label;

    AnimalType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AnimalType fromString(String label) {
        for (AnimalType type : values()) {
            if(type.getLabel().equalsIgnoreCase(label)) return type;
        }
        throw new IllegalArgumentException("Tipo inválido: " + label);
    }
}
