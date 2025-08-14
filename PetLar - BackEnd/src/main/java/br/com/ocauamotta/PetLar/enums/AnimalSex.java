package br.com.ocauamotta.PetLar.enums;

public enum AnimalSex {
    MALE("Macho"),
    FEMALE("Fêmea");

    private final String sex;

    AnimalSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }
}
