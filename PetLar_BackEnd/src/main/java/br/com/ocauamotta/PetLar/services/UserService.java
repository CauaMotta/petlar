package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.User.UserRequestDto;
import br.com.ocauamotta.PetLar.dtos.User.UserResponseDto;
import br.com.ocauamotta.PetLar.mappers.UserMapper;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.validations.UserExistsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Camada de Serviço responsável por implementar a lógica de negócio
 * para a entidade {@code User}.
 */
@Service
public class UserService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserExistsValidation userExistsValidation;

    /**
     * Registra um novo usuário no sistema.
     * <p>
     * O processo envolve:
     * <ol>
     * <li>Mapear o DTO de requisição para a entidade {@code User}.</li>
     * <li>Executar a validação de existência (e-mail duplicado).</li>
     * <li>Criptografar a senha do usuário utilizando o {@code PasswordEncoder}.</li>
     * <li>Salvar o novo registro no repositório.</li>
     * <li>Mapear a entidade persistida para o DTO de resposta.</li>
     * </ol>
     *
     * @param dto O DTO de requisição contendo os dados do novo usuário.
     * @return O {@code UserResponseDto} do usuário recém-registrado, contendo o ID.
     */
    public UserResponseDto save(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);

        userExistsValidation.validate(user);

        String password = encoder.encode(user.getPassword());
        user.setPassword(password);

        return UserMapper.toDTO(repository.insert(user));
    }
}
