package br.com.ocauamotta.PetLar.validations.Animal;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AnimalNotAvailableException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import org.springframework.stereotype.Component;

/**
 * Componente de validação que verifica a disponibilidade de um animal para adoção.
 * <p>
 * Garante que apenas animais com o status {@code DISPONIVEL} possam iniciar
 * um novo processo de solicitação de adoção.
 */
@Component
public class AnimalNotAvailableValidation implements IAnimalValidation {

    /**
     * Valida se o animal está com o status de disponibilidade correto.
     *
     * @param animal O objeto {@code Animal} a ser verificado.
     * @param user   O usuário que tenta a adoção (não utilizado nesta regra específica,
     * mas mantido pela assinatura da interface).
     * @throws AnimalNotAvailableException Se o status do animal for diferente de {@code AdoptionStatus.DISPONIVEL}.
     */
    @Override
    public void validate(Animal animal, User user) {
        if (animal.getStatus() != AdoptionStatus.DISPONIVEL) {
            throw new AnimalNotAvailableException("Este animal está com uma adoção em andamento ou já foi adotado.");
        }
    }
}
