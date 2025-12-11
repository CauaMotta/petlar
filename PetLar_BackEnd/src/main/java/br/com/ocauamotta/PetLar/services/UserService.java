package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.User.*;
import br.com.ocauamotta.PetLar.mappers.UserMapper;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.validations.SamePasswordValidation;
import br.com.ocauamotta.PetLar.validations.UserExistsButDeletedValidation;
import br.com.ocauamotta.PetLar.validations.UserExistsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private TokenService tokenService;

    @Autowired
    private UserExistsValidation userExistsValidation;

    @Autowired
    private UserExistsButDeletedValidation userExistsButDeletedValidation;

    @Autowired
    private SamePasswordValidation samePasswordValidation;

    /**
     * Registra um novo usuário no sistema.
     * <p>
     * O processo envolve:
     * <ol>
     * <li>Mapear o DTO de requisição para a entidade {@code User}.</li>
     * <li>Executar validações de unicidade de e-mail (ativo e logicamente deletado).</li>
     * <li>Criptografar a senha do usuário utilizando o {@code PasswordEncoder}.</li>
     * <li>Definir timestamps de criação e atualização.</li>
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
        userExistsButDeletedValidation.validate(user);

        String password = encoder.encode(user.getPassword());
        user.setPassword(password);

        String time = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString();
        user.setCreatedAt(time);
        user.setUpdatedAt(time);

        return UserMapper.toDTO(repository.insert(user));
    }

    /**
     * Atualiza as informações de um usuário existente.
     * <p>
     * Se o e-mail for alterado, as validações de unicidade são aplicadas e, em caso de sucesso,
     * um novo token JWT é gerado e retornado para o cliente.
     *
     * @param dto O DTO de requisição contendo os dados a serem atualizados.
     * @param user A entidade {@code User} autenticada a ser modificada.
     * @return O {@code UserUpdateResponseDto} contendo o DTO de resposta e um novo token JWT,
     * caso o e-mail tenha sido alterado.
     */
    public UserUpdateResponseDto update(UserUpdateRequestDto dto, User user) {
        boolean emailChanged = !dto.email().equalsIgnoreCase(user.getEmail());

        if (emailChanged) {
            user.setEmail(dto.email());
            userExistsValidation.validate(user);
            userExistsButDeletedValidation.validate(user);
        }
        user.setName(dto.name());
        user.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());

        UserResponseDto userResponseDto = UserMapper.toDTO(repository.save(user));

        String newToken = null;
        if (emailChanged) newToken = tokenService.generateToken(user);

        return new UserUpdateResponseDto(userResponseDto, newToken);
    }

    /**
     * Retorna as informações do usuário autenticado.
     *
     * @param user A entidade {@code User} autenticada.
     * @return O {@code UserResponseDto} contendo as informações do usuario.
     */
    public UserResponseDto findUser(User user) {
        return UserMapper.toDTO(user);
    }

    /**
     * Altera a senha do usuário.
     * <p>
     * O processo envolve:
     * <ol>
     * <li>Executar a validação para garantir que a nova senha seja diferente da atual.</li>
     * <li>Criptografar a nova senha.</li>
     * <li>Salvar o usuário com a nova senha criptografada.</li>
     * </ol>
     *
     * @param dto O DTO contendo a nova senha bruta.
     * @param user A entidade {@code User} autenticada a ser modificada.
     */
    public UserResponseDto changePassword(UserChangePasswordDto dto, User user) {
        user.setPassword(dto.password());

        samePasswordValidation.validate(user);

        String password = encoder.encode(user.getPassword());
        user.setPassword(password);

        return UserMapper.toDTO(repository.save(user));
    }

    /**
     * Realiza a exclusão lógica de um usuário.
     * <p>
     * Em vez de remover o registro, define o campo {@code deletedAt} com o timestamp atual.
     *
     * @param user A entidade {@code User} autenticada a ser excluída logicamente.
     */
    public void delete(User user) {
        user.setDeletedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());
        repository.save(user);
    }
}
