package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mappers.AnimalMapper;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Camada de Serviço responsável por implementar a lógica de negócio
 * para a entidade {@code Animal}.
 * Intermedeia as requisições do controller e o acesso a dados.
 */
@Service
public class AnimalService {

    @Autowired
    private IAnimalRepository repository;

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
     * Define automaticamente a data de registro e o status como {@code DISPONIVEL}.
     *
     * @param dto O DTO de requisição contendo os dados do animal.
     * @return O {@code AnimalResponseDto} do animal recém-salvo, incluindo seu ID.
     */
    public AnimalResponseDto save(AnimalRequestDto dto) {
        Animal entity = AnimalMapper.createEntity(dto);
        entity.setRegistrationDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        entity.setStatus(AdoptionStatus.DISPONIVEL);
        return AnimalMapper.toDTO(repository.insert(entity));
    }

    /**
     * Atualiza um animal existente com os dados fornecidos no DTO.
     * Este método suporta atualizações parciais, modificando apenas os campos não nulos do DTO.
     *
     * @param id O ID do animal a ser atualizado.
     * @param dto O DTO de requisição contendo os dados de atualização.
     * @return O {@code AnimalResponseDto} do animal atualizado.
     * @throws EntityNotFoundException Se o animal com o ID fornecido não for encontrado.
     * @throws IllegalArgumentException Se o DTO não contiver nenhum campo para atualização.
     */
    public AnimalResponseDto update(String id, AnimalRequestDto dto) {
        Animal entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));
        Boolean updated = updateAnimalFields(dto, entity);
        if (!updated) {
            throw new IllegalArgumentException("Nenhum campo para atualização foi informado.");
        }
        return AnimalMapper.toDTO(repository.save(entity));
    }

    /**
     * Remove um animal do sistema pelo seu identificador único.
     *
     * @param id O ID do animal a ser excluído.
     */
    public void delete(String id) {
        repository.deleteById(id);
    }

    /**
     * Método auxiliar privado que utiliza Reflections para aplicar de forma dinâmica
     * apenas os campos não nulos do DTO de requisição ({@code AnimalRequestDto})
     * na entidade persistente ({@code Animal}).
     *
     * @param dto O DTO de requisição com os novos valores.
     * @param entity A entidade {@code Animal} a ser modificada.
     * @return {@code true} se pelo menos um campo foi atualizado; {@code false} caso contrário.
     * @throws IllegalStateException Se ocorrer um erro durante o acesso ou modificação dos campos via Reflection.
     */
    private Boolean updateAnimalFields(AnimalRequestDto dto, Animal entity) {
        Boolean updated = false;

        for ( Field dtoField : dto.getClass().getDeclaredFields() ) {
            dtoField.setAccessible(true);

            if (dtoField.getName().equalsIgnoreCase("id")) {
                continue;
            }

            try {
                Object newValue = dtoField.get(dto);
                if (newValue == null) continue;

                Field entityField;
                try {
                    entityField = entity.getClass().getDeclaredField(dtoField.getName());
                } catch (NoSuchFieldException ex) {
                    continue;
                }

                if(Modifier.isFinal(entityField.getModifiers())) {
                    continue;
                }

                entityField.setAccessible(true);
                entityField.set(entity, newValue);
                updated = true;
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Erro ao tentar atualizar o campo " + dtoField.getName());
            }
        }

        return updated;
    }
}
