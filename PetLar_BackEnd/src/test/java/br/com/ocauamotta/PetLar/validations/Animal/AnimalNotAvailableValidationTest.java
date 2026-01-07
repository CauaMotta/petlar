package br.com.ocauamotta.PetLar.validations.Animal;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AnimalNotAvailableException;
import br.com.ocauamotta.PetLar.models.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(AnimalNotAvailableValidation.class)
class AnimalNotAvailableValidationTest {

    @Autowired
    private AnimalNotAvailableValidation animalNotAvailableValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o animal estiver com status DISPONIVEL.")
    void testValidate_NotShouldThrowException() {
        assertDoesNotThrow(
                () -> animalNotAvailableValidation.validate(createAnimal(AdoptionStatus.DISPONIVEL), null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o animal estiver com status diferente de DISPONIVEL.")
    void testValidate_ShouldThrowException() {
        assertThrows(AnimalNotAvailableException.class,
                () -> animalNotAvailableValidation.validate(createAnimal(AdoptionStatus.PENDENTE), null));
    }

    Animal createAnimal(AdoptionStatus status) {
        return Animal.builder().status(status).build();
    }
}