package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.models.Adoption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de Repositório para a entidade {@code Adoption}.
 * <p>
 * Estende {@code MongoRepository} para fornecer operações CRUD, paginação e
 * consultas personalizadas no banco de dados MongoDB, utilizando {@code String} como
 * o tipo do ID da chave primária.
 */
@Repository
public interface IAdoptionRepository extends MongoRepository<Adoption, String> {
}
