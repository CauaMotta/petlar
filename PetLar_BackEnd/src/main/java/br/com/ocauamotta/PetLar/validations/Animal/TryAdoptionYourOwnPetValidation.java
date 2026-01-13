package br.com.ocauamotta.PetLar.validations.Animal;

import br.com.ocauamotta.PetLar.exceptions.Adoption.TryAdoptionYourOwnPetException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Componente de validação que impede que um usuário adote um animal
 * cadastrado por ele mesmo.
 * <p>
 * Esta regra garante que o autor do anúncio não possa ser o adotante
 * do mesmo registro, mantendo a integridade do fluxo.
 */
@Component
public class TryAdoptionYourOwnPetValidation implements IAnimalValidation {

    /**
     * Valida se o ID do autor do animal é o mesmo ID do usuário que tenta adotar.
     *
     * @param animal O objeto {@code Animal} alvo da tentativa de adoção.
     * @param user O objeto {@code User} (autenticado) que está tentando realizar a adoção.
     * @throws TryAdoptionYourOwnPetException Se o usuário for o próprio autor do cadastro do animal.
     */
    @Override
    public void validate(Animal animal, User user) {
        if (animal.getAuthorId().equals(user.getId())) {
            throw new TryAdoptionYourOwnPetException("Você não pode adotar o mesmo animal que cadastrou.");
        }
    }
}
