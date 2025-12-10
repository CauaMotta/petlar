package br.com.ocauamotta.PetLar.validations;

import br.com.ocauamotta.PetLar.exceptions.DuplicateEmailException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente de validação responsável por verificar se um usuário já existe
 * no banco de dados com o mesmo endereço de e-mail. Garante a unicidade do e-mail.
 */
@Component
public class UserExistsValidation implements IValidation {

    @Autowired
    private IUserRepository repository;

    /**
     * Executa a validação de e-mail duplicado.
     * <p>
     * Utiliza o método {@code existsByEmailIgnoreCase} do repositório para realizar uma busca.
     *
     * @param user O objeto {@code User} a ser validado.
     * @throws DuplicateEmailException Se já existir um usuário cadastrado com o mesmo e-mail.
     */
    @Override
    public void validate(User user) {
        if (repository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateEmailException("Este email já está cadastrado.");
        }
    }
}