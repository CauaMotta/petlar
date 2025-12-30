package br.com.ocauamotta.PetLar.validations.Animal;

import br.com.ocauamotta.PetLar.exceptions.UserWhoIsNotTheOwnerOfTheAnimalException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Componente de validação responsável por garantir que um animal não pertencente
 * pelo usuário seja atualizado.
 */
@Component
public class AnimalOwnerUserValidation implements IAnimalValidation {

    /**
     * Executa a validação do usuário dono do animal pelo ID.
     *
     * @param animal O objeto {@code Animal} contendo o ID do autor.
     * @param user O objeto {@code User} contendo seu ID.
     * @throws UserWhoIsNotTheOwnerOfTheAnimalException Se o autor do animal não for o mesmo usuário.
     */
    @Override
    public void validate(Animal animal, User user) {
        if (!animal.getAuthorId().equals(user.getId())) {
            throw new UserWhoIsNotTheOwnerOfTheAnimalException("Você não possui permissão para alterar este animal.");
        }
    }
}
