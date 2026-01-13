package br.com.ocauamotta.PetLar.validations.User;

import br.com.ocauamotta.PetLar.exceptions.User.UserInactiveException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente de validação responsável por verificar se o usuário ainda possui
 * um registro ativo (não deletado logicamente) no sistema.
 */
@Component
public class UserActiveYetValidation implements IUserValidation{

    @Autowired
    private IUserRepository repository;

    /**
     * Valida se o usuário existe e se o campo {@code deletedAt} é nulo.
     * <p>
     * Consulta o repositório utilizando o ID do usuário para confirmar que
     * a conta não sofreu soft delete.
     *
     * @param user O objeto {@code User}.
     * @throws UserInactiveException Se o usuário não for encontrado ou se estiver
     * marcado como deletado no banco de dados.
     */
    @Override
    public void validate(User user) {
        if (!repository.existsByIdAndDeletedAtIsNull(user.getId())) {
            throw new UserInactiveException("Este usuário não existe ou foi deletado.");
        }
    }
}
