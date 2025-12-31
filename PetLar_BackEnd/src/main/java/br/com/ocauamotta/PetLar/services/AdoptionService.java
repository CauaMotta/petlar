package br.com.ocauamotta.PetLar.services;

import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionRequestDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.AdoptionResponseDto;
import br.com.ocauamotta.PetLar.dtos.Adoption.EditReasonDto;
import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exceptions.EntityNotFoundException;
import br.com.ocauamotta.PetLar.mappers.AdoptionMapper;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import br.com.ocauamotta.PetLar.repositories.IAdoptionRepository;
import br.com.ocauamotta.PetLar.repositories.IAnimalRepository;
import br.com.ocauamotta.PetLar.validations.Adoption.AdoptionOwnershipValidation;
import br.com.ocauamotta.PetLar.validations.Adoption.PendingAdoptionValidation;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalNotAvailableValidation;
import br.com.ocauamotta.PetLar.validations.Animal.TryAdoptionYourOwnPetValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Camada de Serviço responsável por gerenciar o ciclo das adoções.
 * <p>
 * Atua como mediador entre a entidade {@code Animal} e o {@code User} (adotante),
 * garantindo que as solicitações de adoção sigam as regras de negócio definidas
 * e mantenham a consistência dos dados entre os repositórios.
 */
@Service
public class AdoptionService {

    @Autowired
    private IAdoptionRepository adoptionRepository;

    @Autowired
    private IAnimalRepository animalRepository;

    @Autowired
    private TryAdoptionYourOwnPetValidation tryAdoptionYourOwnPetValidation;

    @Autowired
    private AnimalNotAvailableValidation animalNotAvailableValidation;

    @Autowired
    private PendingAdoptionValidation pendingAdoptionValidation;

    @Autowired
    private AdoptionOwnershipValidation adoptionOwnershipValidation;

    /**
     * Inicia o processo de solicitação de adoção para um animal específico.
     * <p>
     * O fluxo de execução consiste em:
     * <ol>
     * <li>Verificar a existência do animal no banco de dados.</li>
     * <li>Validar se o adotante não é o dono do animal.</li>
     * <li>Validar se o animal está com status "DISPONIVEL".</li>
     * <li>Criar o registro de adoção com status "PENDENTE".</li>
     * <li>Atualizar o status do animal para "PENDENTE" para evitar múltiplas solicitações simultâneas.</li>
     * </ol>
     *
     * @param dto O DTO contendo o ID do animal e a justificativa da adoção.
     * @param user O usuário autenticado que deseja realizar a adoção.
     * @return O {@code AdoptionResponseDto} contendo os detalhes da solicitação criada.
     * @throws EntityNotFoundException Se o ID do animal fornecido não existir.
     */
    public AdoptionResponseDto initAdoption(AdoptionRequestDto dto, User user) {
        String time = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString();

        Animal entity = animalRepository.findById(dto.animalId())
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + dto.animalId()));

        tryAdoptionYourOwnPetValidation.validate(entity, user);
        animalNotAvailableValidation.validate(entity, null);

        Adoption adoption = AdoptionMapper.toEntity(dto);
        adoption.setAdopterId(user.getId());
        adoption.setReason(dto.reason());
        adoption.setStatus(AdoptionStatus.PENDENTE);
        adoption.setCreatedAt(time);
        adoption.setUpdatedAt(time);

        Adoption savedAdoption = adoptionRepository.insert(adoption);

        entity.setStatus(AdoptionStatus.PENDENTE);
        animalRepository.save(entity);

        return AdoptionMapper.toDTO(savedAdoption);
    }

    /**
     * Obtém o histórico de solicitações de adoção realizadas pelo usuário autenticado.
     * <p>
     * Utiliza a paginação para otimizar o consumo de recursos e o tempo de resposta,
     * convertendo cada entidade {@code Adoption} da página para {@code AdoptionResponseDto}.
     *
     * @param pageable Configurações de paginação passadas pelo cliente.
     * @param user O usuário autenticado cujas adoções devem ser buscadas.
     * @return Uma página de {@code AdoptionResponseDto} representando o histórico do usuário.
     */
    public Page<AdoptionResponseDto> getMyAdoptionRequests(Pageable pageable, User user) {
        return adoptionRepository.findByAdopterId(user.getId(), pageable).map(AdoptionMapper::toDTO);
    }

    /**
     * Cancela uma solicitação de adoção existente.
     * <p>
     * O processo realiza as seguintes operações:
     * <ol>
     * <li>Recupera a adoção e o animal associado.</li>
     * <li>Valida se o usuário que solicita o cancelamento é o dono da solicitação.</li>
     * <li>Valida se a adoção ainda está pendente (não é possível cancelar algo já aprovado/rejeitado).</li>
     * <li>Altera o status da adoção para {@code CANCELADO}.</li>
     * <li>Libera o animal, voltando seu status para {@code DISPONIVEL}.</li>
     * </ol>
     *
     * @param id O ID da solicitação de adoção a ser cancelada.
     * @param user O usuário autenticado realizando a operação.
     * @return O {@code AdoptionResponseDto} com o status atualizado para cancelado.
     * @throws EntityNotFoundException Se a adoção ou o animal não forem encontrados.
     */
    public AdoptionResponseDto cancelAdoption(String id, User user) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação de adoção não encontrada."));
        Animal entity = animalRepository.findById(adoption.getAnimalId())
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + adoption.getAnimalId()));

        adoptionOwnershipValidation.validate(adoption, user);
        pendingAdoptionValidation.validate(adoption, null);

        adoption.setStatus(AdoptionStatus.CANCELADO);
        adoption.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());
        Adoption savedAdoption = adoptionRepository.save(adoption);

        entity.setStatus(AdoptionStatus.DISPONIVEL);
        animalRepository.save(entity);

        return AdoptionMapper.toDTO(savedAdoption);
    }

    /**
     * Atualiza a justificativa de uma solicitação de adoção pendente.
     * <p>
     * Este método valida se o usuário é o autor da solicitação e se o status
     * permite edição antes de persistir a nova mensagem.
     *
     * @param id O ID da adoção a ser editada.
     * @param dto O DTO contendo a nova justificativa.
     * @param user O usuário autenticado solicitando a alteração.
     * @return O {@code AdoptionResponseDto} com a justificativa e data de atualização modificadas.
     * @throws EntityNotFoundException Se a adoção não existir.
     */
    public AdoptionResponseDto editReason(String id, EditReasonDto dto, User user) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação de adoção não encontrada."));

        adoptionOwnershipValidation.validate(adoption, user);
        pendingAdoptionValidation.validate(adoption, null);

        adoption.setReason(dto.reason());
        adoption.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());

        return AdoptionMapper.toDTO(adoptionRepository.save(adoption));
    }
}
