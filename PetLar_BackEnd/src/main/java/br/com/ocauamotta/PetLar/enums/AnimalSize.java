package br.com.ocauamotta.PetLar.enums;

public enum AnimalSize {
    PEQUENO("pequeno"),
    MEDIO("medio"),
    GRANDE("grande");

    private final String label;

    AnimalSize(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AnimalSize fromString(String label) {
        for (AnimalSize size : values()) {
            if(size.getLabel().equalsIgnoreCase(label)) return size;
        }
        throw new IllegalArgumentException("Porte inválido: " + label);
    }
}
