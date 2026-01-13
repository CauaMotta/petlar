package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exceptions.Adoption.AdoptionAlreadyProcessedException;
import br.com.ocauamotta.PetLar.models.Adoption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(PendingAdoptionValidation.class)
class PendingAdoptionValidationTest {

    @Autowired
    private PendingAdoptionValidation pendingAdoptionValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando a adoção está como PENDENTE.")
    void testValidate_NotShouldThrowException() {
        assertDoesNotThrow(
                () -> pendingAdoptionValidation.validate(createAdoption(AdoptionStatus.PENDENTE), null));
    }

    @Test
    @DisplayName("deve lançar exceção quando a adoção não está como PENDENTE.")
    void testValidate_ShouldThrowException() {
        assertThrows(AdoptionAlreadyProcessedException.class,
                () -> pendingAdoptionValidation.validate(createAdoption(AdoptionStatus.CANCELADO), null));
    }

    Adoption createAdoption(AdoptionStatus status) {
        return Adoption.builder().status(status).build();
    }
}