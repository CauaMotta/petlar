package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.exceptions.Adoption.UserNotOwnershipException;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Componente de validação que verifica se o usuário autenticado é o proprietário do animal.
 * <p>
 * Esta regra garante que apenas o usuário que cadastrou o pet possa gerenciar
 * as solicitações recebidas para ele.
 */
@Component
public class AnimalOwnershipValidation implements IAdoptionValidation{

    /**
     * Compara o ID do proprietário do animal registrado na adoção com o ID do usuário autenticado.
     *
     * @param adoption A entidade de adoção que contém o ID do proprietário do animal.
     * @param user O usuário autenticado tentando realizar a ação de gerenciamento.
     * @throws UserNotOwnershipException Se o usuário não for o dono legal do animal vinculado à adoção.
     */
    @Override
    public void validate(Adoption adoption, User user) {
        if (!adoption.getAnimalOwnerId().equals(user.getId())) {
            throw new UserNotOwnershipException("Não possui permissão para alterar esta socilitação de adoção.");
        }
    }
}
