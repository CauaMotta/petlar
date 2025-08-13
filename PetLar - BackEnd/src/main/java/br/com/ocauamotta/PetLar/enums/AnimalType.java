package br.com.ocauamotta.PetLar.enums;

public enum AnimalType {
    DOG("Cachorro"),
    CAT("Gato");

    private final String type;

    AnimalType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
