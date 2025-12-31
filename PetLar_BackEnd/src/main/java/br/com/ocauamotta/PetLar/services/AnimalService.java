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
import br.com.ocauamotta.PetLar.validations.Animal.AnimalOwnerUserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private AnimalOwnerUserValidation animalOwnerUserValidation;

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
        if (type == null || type.isBlank()) {
            return repository.findByStatus(AdoptionStatus.fromString(status), pageable).map(AnimalMapper::toDTO);
        }
        return repository.findByStatusAndType(AdoptionStatus.fromString(status), AnimalType.fromString(type), pageable).map(AnimalMapper::toDTO);
    }

    /**
     * Busca  uma página de animais pelo usuário autor.
     *
     * @param pageable Objeto que contém informações de paginação e ordenação.
     * @param user O usuário autenticado.
     * @return Uma {@code Page} de {@code AnimalResponseDto} correspondente.
     */
    public Page<AnimalResponseDto> findMyAnimals(Pageable pageable, User user) {
        return repository.findByAuthorId(user.getId(), pageable).map(AnimalMapper::toDTO);
    }

    /**
     * Busca um animal específico pelo seu identificador único.
     *
     * @param id O ID do animal a ser buscado.
     * @return O {@code AnimalResponseDto} correspondente.
     * @throws EntityNotFoundException Se nenhum animal for encontrado com o ID fornecido.
     */
    public AnimalResponseDto findById(String id) {
        Animal entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));
        return AnimalMapper.toDTO(entity);
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

        return AnimalMapper.toDTO(repository.insert(entity));
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
        Animal entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));

        animalOwnerUserValidation.validate(entity, user);

        entity.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());

        updateAnimalFields(AnimalMapper.toEntity(dto), entity);

        return AnimalMapper.toDTO(repository.save(entity));
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
}
