package br.com.ocauamotta.PetLar.validations;

import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.exceptions.SamePasswordException;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Componente de validação responsável por garantir que a nova senha fornecida
 * pelo usuário seja diferente da senha atualmente registrada no sistema.
 * <p>
 * Aplica uma regra de segurança para evitar a reutilização imediata da última senha.
 */
@Component
public class SamePasswordValidation implements IValidation{

    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Executa a validação de senha.
     * <p>
     * O método busca o usuário existente pelo ID e compara a nova senha bruta
     * (contida no objeto {@code user} de entrada) com a senha criptografada
     * (contida no objeto {@code foundUser}).
     *
     * @param user O objeto {@code User} contendo o ID e a nova senha bruta a ser validada.
     * @throws EntityNotFoundException Se o usuário não for encontrado pelo ID antes da comparação.
     * @throws SamePasswordException Se a nova senha for idêntica à senha atual.
     */
    @Override
    public void validate(User user) {
        User foundUser = repository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (encoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new SamePasswordException("A senha tem que ser diferente das antecessoras.");
        }
    }
}
