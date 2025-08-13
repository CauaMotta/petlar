package br.com.ocauamotta.PetLar.enums;

public enum AdoptionStatus {
    ADOPTED("Adotado"),
    AVAILABLE("Disponível");

    private final String status;

    AdoptionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
