package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Recupera uma página de registros de adoção associados a um adotante específico.
     * <p>
     * O Spring Data MongoDB gera automaticamente a consulta baseada no nome do método,
     * aplicando os critérios de paginação e ordenação fornecidos no objeto {@code Pageable}.
     *
     * @param id O id do usuário adotante.
     * @param pageable Configurações de paginação.
     * @return Uma {@code Page<Adoption>} contendo os registros encontrados e metadados da paginação.
     */
    Page<Adoption> findByAdopterId(String id, Pageable pageable);
}
