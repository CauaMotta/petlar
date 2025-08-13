package br.com.ocauamotta.PetLar.enums;

public enum AnimalSize {
    SMALL("Pequeno"),
    MEDIUM("Médio"),
    LARGE("Grande");

    private final String size;

    AnimalSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}
