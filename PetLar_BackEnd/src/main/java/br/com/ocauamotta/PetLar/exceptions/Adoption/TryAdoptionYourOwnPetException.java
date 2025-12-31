package br.com.ocauamotta.PetLar.exceptions.Adoption;

/**
 * Exceção específica lançada quando um usuário tenta iniciar um processo
 * de adoção para um animal que ele mesmo cadastrou.
 */
public class TryAdoptionYourOwnPetException extends AdoptionException {
    public TryAdoptionYourOwnPetException(String message) {
        super(message);
    }
}
