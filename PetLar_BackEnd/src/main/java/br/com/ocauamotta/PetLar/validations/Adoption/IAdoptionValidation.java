package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.User;

/**
 * Interface que define um contrato comum para classes que validam permissões e estados de uma {@code Adoption}.
 */
public interface IAdoptionValidation {
    void validate(Adoption adoption, User user);
}
