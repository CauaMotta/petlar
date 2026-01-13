package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AdoptionAlreadyProcessedException;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Componente de validação que assegura que a solicitação ainda está em estado de espera.
 * <p>
 * Impede alterações em solicitações que já foram processadas, mantendo a imutabilidade do histórico da adoção.
 */
@Component
public class PendingAdoptionValidation implements IAdoptionValidation{

    /**
     * Verifica se o status atual da adoção é {@code PENDENTE}.
     *
     * @param adoption A adoção a ser validada.
     * @param user O usuário (não utilizado nesta regra, mas exigido pela interface).
     * @throws AdoptionAlreadyProcessedException Se a adoção não estiver mais com status pendente.
     */
    @Override
    public void validate(Adoption adoption, User user) {
        if (adoption.getStatus() != AdoptionStatus.PENDENTE) {
            throw new AdoptionAlreadyProcessedException("Não foi possivel alterar o status desta solicitação.");
        }
    }
}
