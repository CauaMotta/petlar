package br.com.ocauamotta.PetLar.validations.Animal;

import br.com.ocauamotta.PetLar.exceptions.Adoption.TryAdoptionYourOwnPetException;
import br.com.ocauamotta.PetLar.exceptions.Animal.UserWhoIsNotTheOwnerOfTheAnimalException;
import br.com.ocauamotta.PetLar.models.Animal;
import br.com.ocauamotta.PetLar.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(TryAdoptionYourOwnPetValidation.class)
class TryAdoptionYourOwnPetValidationTest {

    @Autowired
    private TryAdoptionYourOwnPetValidation tryAdoptionYourOwnPetValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o usuário não for o doador do animal.")
    void testValidate_NotShouldThrowException() {
        assertDoesNotThrow(
                () -> tryAdoptionYourOwnPetValidation.validate(createAnimal("2"), createUser()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário for o doador do animal.")
    void testValidate_ShouldThrowException() {
        assertThrows(TryAdoptionYourOwnPetException.class,
                () -> tryAdoptionYourOwnPetValidation.validate(createAnimal("1"), createUser()));
    }

    Animal createAnimal(String authorId) {
        return Animal.builder().authorId(authorId).build();
    }

    User createUser() {
        return User.builder().id("1").build();
    }
}