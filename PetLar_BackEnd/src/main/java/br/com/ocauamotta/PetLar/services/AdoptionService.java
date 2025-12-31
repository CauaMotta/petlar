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
import br.com.ocauamotta.PetLar.repositories.IUserRepository;
import br.com.ocauamotta.PetLar.validations.Adoption.AdopterOwnershipValidation;
import br.com.ocauamotta.PetLar.validations.Adoption.AnimalOwnershipValidation;
import br.com.ocauamotta.PetLar.validations.Adoption.PendingAdoptionValidation;
import br.com.ocauamotta.PetLar.validations.Animal.AnimalNotAvailableValidation;
import br.com.ocauamotta.PetLar.validations.Animal.TryAdoptionYourOwnPetValidation;
import br.com.ocauamotta.PetLar.validations.User.UserActiveYetValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private IUserRepository userRepository;

    @Autowired
    private TryAdoptionYourOwnPetValidation tryAdoptionYourOwnPetValidation;

    @Autowired
    private AnimalNotAvailableValidation animalNotAvailableValidation;

    @Autowired
    private PendingAdoptionValidation pendingAdoptionValidation;

    @Autowired
    private AdopterOwnershipValidation adopterOwnershipValidation;

    @Autowired
    private UserActiveYetValidation userActiveYetValidation;

    @Autowired
    private AnimalOwnershipValidation animalOwnershipValidation;

    /**
     * Inicia o processo de solicitação de adoção para um animal específico.
     * <p>
     * O fluxo de execução consiste em:
     * <ol>
     * <li>Verificar a existência do animal no banco de dados.</li>
     * <li>Validar se o adotante não é o dono do animal.</li>
     * <li>Validar se o animal está com status "DISPONIVEL".</li>
     * <li>Validar se o autor do animal ainda está ativo no sistema.</li>
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

        Animal entity = getAnimal(dto.animalId());
        tryAdoptionYourOwnPetValidation.validate(entity, user);
        animalNotAvailableValidation.validate(entity, null);
        userActiveYetValidation.validate(createUserReference(entity.getAuthorId()));

        Adoption adoption = AdoptionMapper.toEntity(dto);
        adoption.setAdopterId(user.getId());
        adoption.setAnimalOwnerId(entity.getAuthorId());
        adoption.setReason(dto.reason());
        adoption.setStatus(AdoptionStatus.PENDENTE);
        adoption.setCreatedAt(time);
        adoption.setUpdatedAt(time);

        Adoption savedAdoption = adoptionRepository.insert(adoption);

        entity.setStatus(AdoptionStatus.PENDENTE);
        animalRepository.save(entity);

        return AdoptionMapper.toDTO(savedAdoption, entity, getUser(entity.getAuthorId()), user);
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
    public Page<AdoptionResponseDto> getAdoptionsRequestedByMe(Pageable pageable, User user) {
        Page<Adoption> adoptions = adoptionRepository.findByAdopterId(user.getId(), pageable);
        return mapAdoptionsWithUsers(adoptions);
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
        Adoption adoption = getAdoption(id);
        Animal entity = getAnimal(adoption.getAnimalId());

        adopterOwnershipValidation.validate(adoption, user);
        pendingAdoptionValidation.validate(adoption, null);

        adoption.setStatus(AdoptionStatus.CANCELADO);
        adoption.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());
        Adoption savedAdoption = adoptionRepository.save(adoption);

        entity.setStatus(AdoptionStatus.DISPONIVEL);
        animalRepository.save(entity);

        return AdoptionMapper.toDTO(savedAdoption, entity, getUser(entity.getAuthorId()), user);
    }

    /**
     * Aprova uma solicitação de adoção pendente.
     * <p>
     * Este método encerra o processo de adoção com sucesso, realizando:
     * <ol>
     * <li>A validação de que o executor é o proprietário do animal (Doador).</li>
     * <li>A validação de que a solicitação ainda está pendente.</li>
     * <li>A atualização do status da adoção para {@code APROVADO}.</li>
     * <li>A alteração definitiva do status do animal para {@code ADOTADO}.</li>
     * </ol>
     *
     * @param id O identificador da solicitação de adoção.
     * @param user O usuário autenticado (que deve ser o dono do animal).
     * @return O {@code AdoptionResponseDto} com o status atualizado.
     * @throws EntityNotFoundException Se a adoção ou o animal não forem encontrados.
     */
    public AdoptionResponseDto acceptAdoption(String id, User user) {
        Adoption adoption = getAdoption(id);
        Animal entity = getAnimal(adoption.getAnimalId());

        animalOwnershipValidation.validate(adoption, user);
        pendingAdoptionValidation.validate(adoption, null);

        adoption.setStatus(AdoptionStatus.APROVADO);
        adoption.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());
        Adoption savedAdoption = adoptionRepository.save(adoption);

        entity.setStatus(AdoptionStatus.ADOTADO);
        animalRepository.save(entity);

        return AdoptionMapper.toDTO(savedAdoption, entity, user, getUser(adoption.getAdopterId()));
    }

    /**
     * Recusa uma solicitação de adoção pendente.
     * <p>
     * Este método permite que o proprietário do animal negue uma proposta recebida.
     * O processo realiza as seguintes operações:
     * <ol>
     * <li>Validação de que o executor é o dono do animal (Doador).</li>
     * <li>Verificação se a solicitação ainda permite alteração.</li>
     * <li>Atualização da solicitação para o status {@code RECUSADO}.</li>
     * <li>Retorno do animal para o status {@code DISPONIVEL}, liberando-o para outras solicitações.</li>
     * </ol>
     *
     * @param id O ID da solicitação de adoção.
     * @param user O usuário autenticado (deve ser o proprietário do animal).
     * @return O {@code AdoptionResponseDto} refletindo a recusa e data de atualização.
     * @throws EntityNotFoundException Se a adoção ou o animal não forem encontrados.
     */
    public AdoptionResponseDto denyAdoption(String id, User user) {
        Adoption adoption = getAdoption(id);
        Animal entity = getAnimal(adoption.getAnimalId());

        animalOwnershipValidation.validate(adoption, user);
        pendingAdoptionValidation.validate(adoption, null);

        adoption.setStatus(AdoptionStatus.RECUSADO);
        adoption.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());
        Adoption savedAdoption = adoptionRepository.save(adoption);

        entity.setStatus(AdoptionStatus.DISPONIVEL);
        animalRepository.save(entity);

        return AdoptionMapper.toDTO(savedAdoption, entity, user, getUser(adoption.getAdopterId()));
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
        Adoption adoption = getAdoption(id);

        adopterOwnershipValidation.validate(adoption, user);
        pendingAdoptionValidation.validate(adoption, null);

        adoption.setReason(dto.reason());
        adoption.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toString());

        return AdoptionMapper.toDTO(
                adoptionRepository.save(adoption),
                getAnimal(adoption.getAnimalId()),
                getUser(adoption.getAnimalOwnerId()),
                user);
    }

    /**
     * Obtém o histórico de solicitações de adoção realizadas ao usuário autenticado.
     * <p>
     * Utiliza a paginação para otimizar o consumo de recursos e o tempo de resposta,
     * convertendo cada entidade {@code Adoption} da página para {@code AdoptionResponseDto}.
     *
     * @param pageable Configurações de paginação passadas pelo cliente.
     * @param user O usuário autenticado cujas adoções devem ser buscadas.
     * @return Uma página de {@code AdoptionResponseDto} representando o histórico do usuário.
     */
    public Page<AdoptionResponseDto> getRequestsForMyAnimals(Pageable pageable, User user) {
        Page<Adoption> adoptions = adoptionRepository.findByAnimalOwnerId(user.getId(), pageable);
        return mapAdoptionsWithUsers(adoptions);
    }

    /**
     * Cria uma instância de referência da entidade {@code User} contendo apenas o ID.
     *
     * @param id O identificador único do usuário.
     * @return Uma instância de {@code User} "parcial" contendo apenas o campo {@code id} preenchido.
     */
    private User createUserReference(String id) {
        return User.builder().id(id).build();
    }

    /**
     * Recupera um usuário pelo ID ou lança exceção.
     */
    private User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));
    }

    /**
     * Recupera um animal pelo ID ou lança exceção.
     */
    private Animal getAnimal(String id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum registro encontrado com ID - " + id));
    }

    /**
     * Recupera uma solicitação de adoção pelo ID ou lança exceção.
     */
    private Adoption getAdoption(String id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação de adoção não encontrada."));
    }

    /**
     * Enriquece uma página de registros de adoção com dados completos de Animais, Adotantes e Doadores.
     * <p>
     * Para evitar múltiplas consultas ao banco de dados (problema N+1), este método:
     * <ol>
     * <li>Extrai todos os IDs únicos de Adotantes e Doadores em um único {@code Set}.</li>
     * <li>Extrai todos os IDs únicos de Animais.</li>
     * <li>Realiza buscas em lote {@code findAllById} para carregar todos os objetos necessários.</li>
     * <li>Cria mapas de busca em memória para associar as entidades rapidamente.</li>
     * <li>Valida a integridade das referências antes de realizar o mapeamento para DTO.</li>
     * </ol>
     *
     * @param adoptions Uma página de entidades {@code Adoption}.
     * @return Uma página de {@code AdoptionResponseDto} totalmente populada.
     * @throws EntityNotFoundException Caso algum objeto relacionado não seja encontrado no banco.
     */
    private Page<AdoptionResponseDto> mapAdoptionsWithUsers(Page<Adoption> adoptions) {
        Set<String> userIds = adoptions.getContent().stream()
                .flatMap(adoption -> Stream.of(
                        adoption.getAdopterId(),
                        adoption.getAnimalOwnerId()
                ))
                .collect(Collectors.toSet());

        Set<String> animalIds = adoptions.getContent().stream()
                .map(Adoption::getAnimalId)
                .collect(Collectors.toSet());

        Map<String, User> usersMap = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Map<String, Animal> animalsMap = animalRepository.findAllById(animalIds)
                .stream().collect(Collectors.toMap(Animal::getId, Function.identity()));

        return adoptions.map(adoption -> {

            Animal animal = animalsMap.get(adoption.getAnimalId());
            User adopter = usersMap.get(adoption.getAdopterId());
            User animalOwner = usersMap.get(adoption.getAnimalOwnerId());

            if (adopter == null || animalOwner == null) {
                throw new EntityNotFoundException("Usuário relacionado à adoção não encontrado.");
            }

            return AdoptionMapper.toDTO(
                    adoption,
                    animal,
                    animalOwner,
                    adopter
            );
        });
    }
}
