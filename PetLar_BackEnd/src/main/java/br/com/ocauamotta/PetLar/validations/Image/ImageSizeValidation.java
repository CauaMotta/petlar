package br.com.ocauamotta.PetLar.validations.Image;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Validador responsável por restringir o tamanho físico do arquivo.
 * <p>
 * Atualmente, limita o carregamento a arquivos de no máximo 2MB.
 */
@Component
public class ImageSizeValidation implements IImageValidation {

    /**
     * Verifica se o tamanho do arquivo excede o limite de 2.000.000 bytes.
     *
     * @param image O arquivo a ser validado.
     * @throws IllegalArgumentException Se a imagem for maior que 2MB.
     */
    @Override
    public void validate(MultipartFile image) {
        if (image.getSize() > 2_000_000) {
            throw new IllegalArgumentException("Imagem muito grande.");
        }
    }
}
