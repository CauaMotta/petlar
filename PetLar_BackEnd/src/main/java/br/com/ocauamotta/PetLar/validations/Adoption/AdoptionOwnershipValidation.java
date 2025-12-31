package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.exceptions.Adoption.UserNotAdopterException;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Componente de validação que verifica a propriedade de uma solicitação de adoção.
 * <p>
 * Garante que apenas o usuário que criou a solicitação (o adotante original)
 * tenha permissão para realizar ações.
 */
@Component
public class AdoptionOwnershipValidation implements IAdoptionValidation {

    /**
     * Compara o ID do adotante registrado na solicitação com o ID do usuário autenticado.
     *
     * @param adoption A adoção alvo da operação.
     * @param user O usuário autenticado tentando a operação.
     * @throws UserNotAdopterException Se o usuário não for o autor da solicitação de adoção.
     */
    @Override
    public void validate(Adoption adoption, User user) {
        if (!adoption.getAdopterId().equals(user.getId())) {
            throw new UserNotAdopterException("Não possui permissão para alterar esta socilitação de adoção.");
        }
    }
}
