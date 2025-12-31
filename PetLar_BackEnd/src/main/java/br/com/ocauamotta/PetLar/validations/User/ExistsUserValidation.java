package br.com.ocauamotta.PetLar.validations.User;

import br.com.ocauamotta.PetLar.exceptions.User.DuplicateEmailException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente de validação responsável por verificar se um usuário já existe
 * no banco de dados com o mesmo endereço de e-mail. Garante a unicidade do e-mail.
 */
@Component
public class ExistsUserValidation implements IUserValidation {

    @Autowired
    private IUserRepository repository;

    /**
     * Executa a validação de e-mail duplicado.
     * <p>
     * Utiliza o método {@code existsByEmailIgnoreCaseAndDeletedAtIsNull} do repositório para realizar uma busca por
     * usuarios não deletados.
     *
     * @param user O objeto {@code User} a ser validado.
     * @throws DuplicateEmailException Se já existir um usuário cadastrado com o mesmo e-mail.
     */
    @Override
    public void validate(User user) {
        if (repository.existsByEmailIgnoreCaseAndDeletedAtIsNull(user.getEmail())) {
            throw new DuplicateEmailException("Este email já está cadastrado.");
        }
    }
}