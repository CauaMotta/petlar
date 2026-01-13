package br.com.ocauamotta.PetLar.mappers;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
import br.com.ocauamotta.PetLar.dtos.Animal.AnimalSummaryDto;
import br.com.ocauamotta.PetLar.dtos.User.UserSummaryDto;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
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
    public static AdoptionResponseDto toDTO(Adoption entity, Animal animal, User animalOwner, User adopter) {
        if (entity == null) return null;

        return new AdoptionResponseDto(
                entity.getId(),
                entity.getStatus(),
                toAnimalSummary(animal),
                new UserSummaryDto(animalOwner.getId(), animalOwner.getName()),
                new UserSummaryDto(adopter.getId(), adopter.getName()),
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

    /**
     * Converte uma entidade {@code Animal} para um objeto de transferência de dados simplificado {@code AnimalSummaryDto}.
     * <p>
     * Este método é utilizado para fornecer uma visão resumida do animal em contextos onde
     * o objeto completo não é necessário.
     *
     * @param entity A entidade {@code Animal}.
     * @return Um {@code AnimalSummaryDto} populado ou {@code null} se a entidade fornecida for nula.
     */
    private static AnimalSummaryDto toAnimalSummary(Animal entity) {
        if (entity == null) return null;

        return new AnimalSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getBirthDate(),
                entity.getWeight(),
                entity.getType(),
                entity.getSex(),
                entity.getSize(),
                entity.getImagePath(),
                entity.getDescription()
        );
    }
}
