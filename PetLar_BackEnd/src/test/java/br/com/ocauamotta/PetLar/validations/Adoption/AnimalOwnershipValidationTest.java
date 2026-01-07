package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.enums.AdoptionStatus;
import br.com.ocauamotta.PetLar.exceptions.Adoption.UserNotOwnershipException;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(AnimalOwnershipValidation.class)
class AnimalOwnershipValidationTest {

    @Autowired
    private AnimalOwnershipValidation animalOwnershipValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o usuário for o doador do animal.")
    void testValidate_NotShouldThrowException() {
        assertDoesNotThrow(
                () -> animalOwnershipValidation.validate(createAdoption("1"), createUser()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for o doador do animal.")
    void testValidate_ShouldThrowException() {
        assertThrows(UserNotOwnershipException.class,
                () -> animalOwnershipValidation.validate(createAdoption("2"), createUser()));
    }

    Adoption createAdoption(String animalOwnerId) {
        return Adoption.builder().animalOwnerId(animalOwnerId).build();
    }

    User createUser() {
        return User.builder().id("1").build();
    }
}