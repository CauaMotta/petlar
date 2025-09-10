package br.com.ocauamotta.PetLar.enums;

public enum AnimalSex {
    MALE("Macho"),
    FEMALE("Fêmea");

    private final String label;

    AnimalSex(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AnimalSex fromLabel(String label) {
        for (AnimalSex sex : values()) {
            if(sex.getLabel().equalsIgnoreCase(label)) return sex;
        }
        throw new IllegalArgumentException("Sexo inválido: " + label);
    }
}
