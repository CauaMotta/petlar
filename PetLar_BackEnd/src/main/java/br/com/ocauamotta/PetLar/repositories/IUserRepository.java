package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface de Repositório para a entidade {@code User}.
 * Estende {@code MongoRepository} para fornecer operações CRUD, paginação e
 * consultas personalizadas no banco de dados MongoDB.
 * <p>
 * O tipo de entidade gerenciada é {@code User}
 * e o tipo da chave primária (ID) é {@code String}.
 */
@Repository
public interface IUserRepository extends MongoRepository<User, String> {
    /**
     * Busca um usuário ativo pelo seu email.
     * <p>
     * Este método é essencial para o processo de autenticação do Spring Security.
     *
     * @param email O email do usuário a ser buscado.
     * @return Um objeto {@code Optional} contendo o {@code UserDetails}
     * se um usuário com o email fornecido for encontrado. Retorna um {@code Optional} vázio
     * se o usuário não existir.
     */
    Optional<UserDetails> findByEmailIgnoreCaseAndDeletedAtIsNull(String email);

    /**
     * Verifica a existência de um usuário ativo no banco de dados com o email fornecido.
     * <p>
     * Um usuário é considerado ativo quando o campo {@code deletedAt} é nulo.
     * Utilizado para prevenir o cadastro de e-mails que já estão em uso por contas ativas.
     *
     * @param email O email a ser verificado.
     * @return {@code true} se um usuário ativo com o email já existir; {@code false} caso contrário.
     */
    Boolean existsByEmailIgnoreCaseAndDeletedAtIsNull(String email);

    /**
     * Verifica a existência de um usuário logicamente deletado no banco de dados com o email fornecido.
     * <p>
     * Um usuário é considerado deletado quando o campo {@code deletedAt} possui um valor diferente de nulo.
     * Utilizado para tratar separadamente e-mails de contas inativas.
     *
     * @param email O email a ser verificado.
     * @return {@code true} se um usuário deletado com o email já existir; {@code false} caso contrário.
     */
    Boolean existsByEmailIgnoreCaseAndDeletedAtIsNotNull(String email);

    /**
     * Verifica se existe um registro ativo para o ID fornecido.
     * <p>
     * Este método realiza uma consulta otimizada para confirmar se o usuário
     * não apenas existe no banco de dados, mas também se sua conta não foi
     * marcada como excluída.
     *
     * @param id O ID do usuário a ser verificado.
     * @return {@code true} se um usuário ativo com este ID existir;
     * {@code false} caso ele não exista ou tenha sido deletado.
     */
    Boolean existsByIdAndDeletedAtIsNull(String id);
}
