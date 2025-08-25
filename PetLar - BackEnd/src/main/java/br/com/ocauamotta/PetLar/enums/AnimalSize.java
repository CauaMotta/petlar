package br.com.ocauamotta.PetLar.enums;

public enum AnimalSize {
    SMALL("Pequeno"),
    MEDIUM("Médio"),
    LARGE("Grande");

    private final String label;

    AnimalSize(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AnimalSize fromLabel(String label) {
        for (AnimalSize size : values()) {
            if(size.getLabel().equalsIgnoreCase(label)) return size;
        }
        throw new IllegalArgumentException("Tamanho inválido: " + label);
    }
}
