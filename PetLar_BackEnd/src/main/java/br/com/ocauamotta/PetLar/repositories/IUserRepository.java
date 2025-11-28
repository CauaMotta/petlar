package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

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
     *
     * <p>Este método é essencial para o processo de autenticação do Spring Security,
     * pois retorna um objeto {@code UserDetails}
     * que contém as informações necessárias para verificar as credenciais.
     *
     * @param email O email do usuário a ser buscado.
     * @return Um objeto {@code UserDetails}
     * que representa o usuário encontrado, ou {@code null} se o usuário não existir.
     */
    UserDetails findByEmail(String email);
}
