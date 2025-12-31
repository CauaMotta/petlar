package br.com.ocauamotta.PetLar.mappers;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
import br.com.ocauamotta.PetLar.models.Adoption;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária responsável por converter objetos entre a camada de Entidade
 * {@code Adoption} e os objetos de Transferência de Dados (DTOs).
 * <p>
 * Garante a separação e o isolamento entre o modelo de domínio e os modelos de API.
 */
@Component
public class AdoptionMapper {

    /**
     * Construtor privado para evitar a instanciação desta classe utilitária.
     */
    private AdoptionMapper() {}

    /**
     * Converte a entidade de domínio {@code Adoption} em um DTO de Resposta {@code AdoptionResponseDto}.
     *
     * @param entity A entidade {@code Adoption} a ser convertida.
     * @return O DTO de resposta correspondente, ou {@code null} se a entidade de entrada for nula.
     */
    public static AdoptionResponseDto toDTO(Adoption entity) {
        if (entity == null) return null;

        return new AdoptionResponseDto(
                entity.getId(),
                entity.getStatus(),
                entity.getAnimalId(),
                entity.getAnimalOwnerId(),
                entity.getAdopterId(),
                entity.getReason(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Converte um DTO de Requisição {@code AdoptionRequestDto} em uma nova entidade {@code Adoption}.
     *
     * @param dto O DTO de requisição da adoção.
     * @return A nova entidade {@code Adoption}.
     */
    public static Adoption toEntity(AdoptionRequestDto dto) {
        if (dto == null) return null;

        return Adoption.builder()
                .animalId(dto.animalId())
                .reason(dto.reason())
                .build();
    }
}
