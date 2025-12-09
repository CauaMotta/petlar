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
     * Busca um usuário pelo seu email.
     * <p>
     * Este método é essencial para o processo de autenticação do Spring Security.
     *
     * @param email O email do usuário a ser buscado.
     * @return Um objeto {@code Optional} contendo o {@code UserDetails}
     * se um usuário com o email fornecido for encontrado. Retorna um {@code Optional} vázio
     * se o usuário não existir.
     */
    Optional<UserDetails> findByEmail(String email);
}
