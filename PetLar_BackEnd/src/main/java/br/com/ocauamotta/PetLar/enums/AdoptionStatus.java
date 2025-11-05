package br.com.ocauamotta.PetLar.enums;

public enum AdoptionStatus {
    ADOTADO("adotado"),
    PENDENTE("pendente"),
    DISPONIVEL("disponivel");

    private final String label;

    AdoptionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static AdoptionStatus fromString(String label) {
        for (AdoptionStatus status : values()) {
            if(status.getLabel().equalsIgnoreCase(label)) return status;
        }
        throw new IllegalArgumentException("Status inválido: " + label);
    }
}
