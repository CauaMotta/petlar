package br.com.ocauamotta.PetLar.enums;

public enum AnimalType {
    DOG("Cachorro"),
    CAT("Gato");

    private final String label;

    AnimalType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AnimalType fromLabel(String label) {
        for (AnimalType type : values()) {
            if(type.getLabel().equalsIgnoreCase(label)) return type;
        }
        throw new IllegalArgumentException("tipo inválido: " + label);
    }
}
