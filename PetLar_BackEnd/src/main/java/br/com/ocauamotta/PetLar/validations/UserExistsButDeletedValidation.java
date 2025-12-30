package br.com.ocauamotta.PetLar.validations;

import br.com.ocauamotta.PetLar.exceptions.DuplicateEmailException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente de validação que verifica se um e-mail já está sendo usado por um usuário
 * que foi marcado como deletado (soft delete).
 */
@Component
public class UserExistsButDeletedValidation implements IValidation {

    @Autowired
    private IUserRepository repository;

    /**
     * Executa a validação de existência de e-mail em contas logicamente deletadas.
     * <p>
     * Utiliza o método de consulta customizado {@code existsByEmailIgnoreCaseAndDeletedAtIsNotNull}
     * para verificar e-mails em registros inativos.
     *
     * @param user O objeto {@code User} a ser validado.
     * @throws DuplicateEmailException Se o e-mail já estiver cadastrado em uma conta que foi logicamente deletada.
     */
    @Override
    public void validate(User user) {
        if (repository.existsByEmailIgnoreCaseAndDeletedAtIsNotNull(user.getEmail())) {
            throw new DuplicateEmailException("Uma conta com este email foi apagada.");
        }
    }
}