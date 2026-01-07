package br.com.ocauamotta.PetLar.validations.Adoption;

import br.com.ocauamotta.PetLar.exceptions.Adoption.UserNotOwnershipException;
import br.com.ocauamotta.PetLar.models.Adoption;
import br.com.ocauamotta.PetLar.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(AdopterOwnershipValidation.class)
class AdopterOwnershipValidationTest {

    @Autowired
    private AdopterOwnershipValidation adopterOwnershipValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o usuário for o autor da solicitação de adoção.")
    void testValidate_NotShouldThrowException() {
        assertDoesNotThrow(
                () -> adopterOwnershipValidation.validate(createAdoption("1"), createUser()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for o autor da solicitação de adoção.")
    void testValidate_ShouldThrowException() {
        assertThrows(UserNotOwnershipException.class,
                () -> adopterOwnershipValidation.validate(createAdoption("2"), createUser()));
    }

    Adoption createAdoption(String adopterId) {
        return Adoption.builder().adopterId(adopterId).build();
    }

    User createUser() {
        return User.builder().id("1").build();
    }
}