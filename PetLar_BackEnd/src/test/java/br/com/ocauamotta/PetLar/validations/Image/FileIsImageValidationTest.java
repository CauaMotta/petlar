package br.com.ocauamotta.PetLar.validations.Image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(FileIsImageValidation.class)
class FileIsImageValidationTest {

    @Autowired
    private FileIsImageValidation fileIsImageValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o arquivo recebido for do tipo imagem.")
    void testValidate_NotShouldThrowException() {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                new byte[1_000_000]);

        assertDoesNotThrow(() -> fileIsImageValidation.validate(image));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o arquivo recebido não for do tipo imagem.")
    void testValidate_ShouldThrowException() {
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                new byte[1_000_000]);

        assertThrows(IllegalArgumentException.class, () -> fileIsImageValidation.validate(image));
    }
}