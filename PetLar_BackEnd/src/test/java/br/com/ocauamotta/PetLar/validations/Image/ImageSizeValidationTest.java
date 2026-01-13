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
@Import(ImageSizeValidation.class)
class ImageSizeValidationTest {

    @Autowired
    private ImageSizeValidation imageSizeValidation;

    @Test
    @DisplayName("Não deve lançar exceção quando o arquivo for menor que 2MB")
    void testValidate_NotShouldThrowException() {
        byte[] content = new byte[1_000_000];

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                content);

        assertDoesNotThrow(() -> imageSizeValidation.validate(image));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o arquivo for maior que 2MB")
    void testValidate_ShouldThrowException() {
        byte[] content = new byte[2_500_000];

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                content);

        assertThrows(IllegalArgumentException.class, () -> imageSizeValidation.validate(image));
    }
}