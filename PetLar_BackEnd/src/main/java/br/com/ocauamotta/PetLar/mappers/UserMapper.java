package br.com.ocauamotta.PetLar.mappers;

import br.com.ocauamotta.PetLar.dtos.User.UserRequestDto;
import br.com.ocauamotta.PetLar.dtos.User.UserResponseDto;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária responsável por converter objetos entre a camada de Entidade
 * {@code User} e os DTOs específicos do usuário.
 * <p>
 * Garante que a entidade de domínio não seja exposta diretamente nas camadas de Controller/Service.
 */
@Component
public class UserMapper {

    /**
     * Construtor privado para evitar a instanciação desta classe utilitária.
     */
    private UserMapper() {}

    /**
     * Converte a entidade de domínio {@code User} em um DTO de Resposta {@code UserResponseDto}.
     * <p>
     * Por segurança, este DTO de resposta omite campos sensíveis como a senha.
     *
     * @param entity A entidade {@code User} a ser convertida.
     * @return O DTO de resposta correspondente, ou {@code null} se a entidade de entrada for nula.
     */
    public static UserResponseDto toDTO(User entity) {
        if (entity == null) return null;

        return new UserResponseDto(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    /**
     * Converte um DTO de Requisição {@code UserRequestDto} em uma nova entidade {@code User}.
     *
     * @param dto O DTO de requisição do cliente.
     * @return A nova entidade {@code User}.
     */
    public static User toEntity(UserRequestDto dto) {
        if (dto == null) return null;

        return User.builder()
                .email(dto.email())
                .password(dto.password())
                .name(dto.name())
                .build();
    }
}
