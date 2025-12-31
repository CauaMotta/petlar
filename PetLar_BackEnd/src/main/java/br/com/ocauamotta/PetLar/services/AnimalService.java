package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.Animal.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mappers.AnimalMapper;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalNotAvailableValidation;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalOwnerUserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Camada de Serviço responsável por implementar a lógica de negócio
 * para a entidade {@code Animal}.
 * Intermedeia as requisições do controller e o acesso a dados.
 */
@Service
public class AnimalService {

    @Autowired
    private IAnimalRepository repository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private AnimalOwnerUserValidation animalOwnerUserValidation;

    @Autowired
    private AnimalNotAvailableValidation animalNotAvailableValidation;

    /**
     * Busca uma página de animais, permitindo a filtragem por status de adoção
     * e, opcionalmente, por tipo de animal.
     *
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @param status O status de adoção do animal.
     * @param type O tipo de animal opcional para filtro.
     * @return Uma {@code Page} de {@code AnimalResponseDto} correspondente aos critérios de filtro.
     */
    public Page<AnimalResponseDto> findAll(Pageable pageable, String status, String type) {
        Page<Animal> animals;

        if (type == null || type.isBlank()) {
            animals = repository.findByStatus(AdoptionStatus.fromString(status), pageable);
        } else {
            animals = repository.findByStatusAndType(
                    AdoptionStatus.fromString(status),
                    AnimalType.fromString(type),
                    pageable);
        }

        return mapAnimalsWithAuthors(animals);
    }

    /**
     * Busca  uma página de animais pelo usuário autor.
     *
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @param user O usuário autenticado.
     * @return Uma {@code Page} de {@code AnimalResponseDto} correspondente.
     */
    public Page<AnimalResponseDto> findMyAnimals(Pageable pageable, User user) {
        return repository.findByAuthorId(user.getId(), pageable)
                .map(animal -> AnimalMapper.toDTO(animal, user));
    }

    /**
     * Busca um animal específico pelo seu identificador único.
     *
     * @param id O ID do animal a ser buscado.
     * @return O {@code AnimalResponseDto} correspondente.
     * @throws EntityNotFoundException Se nenhum animal for encontrado com o ID fornecido.
     */
    public AnimalResponseDto findById(String id) {
        Animal entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));
        User user = userRepository.findById(entity.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + entity.getAuthorId()));
        return AnimalMapper.toDTO(entity, user);
    }

    /**
     * Salva um novo animal no sistema.
     * Define automaticamente a data de registro, data de atualização,
     * id do autor e o status como {@code DISPONIVEL}.
     *
     * @param dto O DTO de requisição contendo os dados do animal.
     * @param user O usuário autenticado.
     * @return O {@code AnimalResponseDto} do animal recém-salvo, incluindo seu ID.
     */
    public AnimalResponseDto save(AnimalRequestDto dto, User user) {
        String time = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString();

        Animal entity = AnimalMapper.toEntity(dto);

        entity.setAuthorId(user.getId());
        entity.setStatus(AdoptionStatus.DISPONIVEL);
        entity.setCreatedAt(time);
        entity.setUpdatedAt(time);

        return AnimalMapper.toDTO(repository.insert(entity), user);
    }

    /**
     * Atualiza um animal existente com os dados fornecidos no DTO.
     *
     * @param id O ID do animal a ser atualizado.
     * @param dto O DTO de requisição contendo os dados de atualização.
     * @param user O usuário autenticado.
     * @return O {@code AnimalResponseDto} do animal atualizado.
     * @throws EntityNotFoundException Se o animal com o ID fornecido não for encontrado.
     */
    public AnimalResponseDto update(String id, AnimalRequestDto dto, User user) {
        Animal entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));

        animalOwnerUserValidation.validate(entity, user);
        animalNotAvailableValidation.validate(entity, null);

        entity.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());

        updateAnimalFields(AnimalMapper.toEntity(dto), entity);

        return AnimalMapper.toDTO(repository.save(entity), user);
    }

    /**
     * Remove um animal do sistema pelo seu identificador único.
     *
     * @param id O ID do animal a ser excluído.
     * @param user O usuário autenticado.
     */
    public void delete(String id, User user) {
        Animal entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));

        animalOwnerUserValidation.validate(entity, user);
        animalNotAvailableValidation.validate(entity, null);

        repository.delete(entity);
    }

    /**
     * Método auxiliar privado que utiliza Reflections para aplicar de forma dinâmica
     * os campos da requisição com os novos dados na entidade persistente {@code Animal}.
     *
     * @param updated A entidade gerada a partir do DTO de requisição com os novos valores.
     * @param entity A entidade {@code Animal} a ser modificada.
     * @throws IllegalStateException Se ocorrer um erro durante o acesso ou modificação dos campos via Reflection.
     */
    private void updateAnimalFields(Animal updated, Animal entity) {
        for ( Field field : updated.getClass().getDeclaredFields() ) {
            if (field.getName().equalsIgnoreCase("id")) continue;
            field.setAccessible(true);

            try {
                Object newValue = field.get(updated);
                if (newValue == null) continue;

                Field entityField = entity.getClass().getDeclaredField(field.getName());
                entityField.setAccessible(true);
                entityField.set(entity, newValue);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                throw new IllegalStateException("Erro ao tentar atualizar o campo " + field.getName());
            }
        }
    }

    /**
     * Enriquece uma página de animais com os dados de seus respectivos autores (usuários).
     * <p>
     * Este método utiliza uma estratégia de busca em lote (Batch Loading) para evitar o
     * problema de performance N+1. O processo consiste em:
     * <ol>
     * <li>Coletar todos os IDs únicos de autores presentes na página de animais.</li>
     * <li>Realizar uma única consulta ao repositório para buscar todos esses usuários.</li>
     * <li>Mapear os usuários em um {@code Map} para acesso rápido O(1).</li>
     * <li>Converter cada {@code Animal} para {@code AnimalResponseDto} injetando seu autor correspondente.</li>
     * </ol>
     *
     * @param animals Uma página de entidades {@code Animal}.
     * @return Uma nova {@code Page} contendo {@code AnimalResponseDto} com os dados dos autores populados.
     * @throws EntityNotFoundException Se um autor referenciado no animal não for encontrado no banco de dados.
     */
    private Page<AnimalResponseDto> mapAnimalsWithAuthors(Page<Animal> animals) {
        Set<String> authorIds = animals.getContent().stream()
                .map(Animal::getAuthorId)
                .collect(Collectors.toSet());

        Map<String, User> authorsMap = userRepository.findAllById(authorIds)
                .stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return animals.map(animal -> {
            User author = authorsMap.get(animal.getAuthorId());

            if (author == null) {
                throw new EntityNotFoundException("Autor não encontrado");
            }

            return AnimalMapper.toDTO(animal, author);
        });
    }
}
