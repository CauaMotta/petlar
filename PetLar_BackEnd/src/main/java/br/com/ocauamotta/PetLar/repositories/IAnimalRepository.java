package br.com.ocauamotta.PetLar.repositories;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de Repositório para a entidade {@code Animal}.
 * Estende {@code MongoRepository} para fornecer operações CRUD, paginação e
 * consultas personalizadas no banco de dados MongoDB, utilizando {@code String} como
 * o tipo do ID da chave primária.
 */
@Repository
public interface IAnimalRepository extends MongoRepository<Animal, String> {
    /**
     * Busca uma página de animais com base no status de adoção fornecido.
     *
     * @param adoptionStatus O status de adoção pelo qual filtrar.
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @return Uma {@code Page} contendo os animais que correspondem ao status.
     */
    Page<Animal> findByStatus(AdoptionStatus adoptionStatus, Pageable pageable);

    /**
     * Busca uma página de animais que correspondem a um status de adoção e a um tipo de animal específico.
     *
     * @param adoptionStatus O status de adoção pelo qual filtrar.
     * @param type O tipo de animal pelo qual filtrar.
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @return Uma {@code Page} contendo os animais que correspondem ao status e ao tipo.
     */
    Page<Animal> findByStatusAndType(AdoptionStatus adoptionStatus, AnimalType type, Pageable pageable);

    /**
     * Busca uma página de animais que correspondem ao ID do usuário autor.
     *
     * @param id O ID do usuário autor.
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @return Uma {@code Page} contendo os animais que correspondem ao status e ao tipo.
     */
    Page<Animal> findByAuthorId(String id, Pageable pageable);
}
