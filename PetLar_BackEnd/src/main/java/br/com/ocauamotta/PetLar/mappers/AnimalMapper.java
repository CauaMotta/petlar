package br.com.ocauamotta.PetLar.mappers;

import br.com.ocauamotta.PetLar.dtos.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.AnimalResponseDto;
import br.com.ocauamotta.PetLar.enums.AnimalSex;
import br.com.ocauamotta.PetLar.enums.AnimalSize;
import br.com.ocauamotta.PetLar.enums.AnimalType;
import br.com.ocauamotta.PetLar.models.Animal;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária responsável por converter objetos entre a camada de Entidade
 * {@code Animal} e os objetos de Transferência de Dados (DTOs).
 * <p>
 * Garante a separação e o isolamento entre o modelo de domínio e os modelos de API.
 */
@Component
public class AnimalMapper {

    /**
     * Construtor privado para evitar a instanciação desta classe utilitária.
     */
    private AnimalMapper() {
    }

    /**
     * Converte a entidade de domínio {@code Animal} em um DTO de Resposta {@code AnimalResponseDto}.
     *
     * @param entity A entidade {@code Animal} a ser convertida.
     * @return O DTO de resposta correspondente, ou {@code null} se a entidade de entrada for nula.
     */
    public static AnimalResponseDto toDTO(Animal entity) {
        if (entity == null) return null;

        return new AnimalResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDob(),
                entity.getWeight(),
                entity.getType(),
                entity.getSex(),
                entity.getSize(),
                entity.getRegistrationDate(),
                entity.getStatus(),
                entity.getDescription()
        );
    }

    /**
     * Converte um DTO de Requisição {@code AnimalRequestDto} em uma nova entidade {@code Animal}.
     *
     * @param dto O DTO de requisição do animal.
     * @return A nova entidade {@code Animal} (sem ID, data de registro ou status definidos).
     */
    public static Animal toEntity(AnimalRequestDto dto) {
        if (dto == null) return null;

        return Animal.builder()
                .name(dto.name())
                .dob(dto.dob())
                .weight(dto.weight())
                .type(AnimalType.fromString(dto.type()))
                .sex(AnimalSex.fromString(dto.sex()))
                .size(AnimalSize.fromString(dto.size()))
                .description(dto.description())
                .build();
    }
}
