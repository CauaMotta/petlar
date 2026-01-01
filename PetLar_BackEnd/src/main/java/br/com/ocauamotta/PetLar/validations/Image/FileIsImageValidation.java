package br.com.ocauamotta.PetLar.validations.Image;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Validador de tipo de conteúdo.
 * <p>
 * Garante que o cabeçalho {@code Content-Type} do arquivo enviado comece
 * com o prefixo "image/", permitindo apenas formatos como JPEG, PNG, WEBP, etc.
 */
@Component
public class FileIsImageValidation implements IImageValidation {

    /**
     * Valida se o tipo MIME do arquivo pertence à família de imagens.
     *
     * @param image O arquivo recebido.
     * @throws IllegalArgumentException Se o arquivo não possuir um tipo MIME de imagem válido.
     */
    @Override
    public void validate(MultipartFile image) {
        if (!image.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo não é uma imagem.");
        }
    }
}
