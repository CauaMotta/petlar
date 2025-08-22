package br.com.ocauamotta.PetLar.enums;

public enum AdoptionStatus {
    ADOPTED("Adotado"),
    AVAILABLE("Disponível");

    private final String label;

    AdoptionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AdoptionStatus fromLabel(String label) {
        for (AdoptionStatus status : values()) {
            if(status.getLabel().equalsIgnoreCase(label)) return status;
        }
        throw new IllegalArgumentException("typeo inválido: " + label);
    }
}
