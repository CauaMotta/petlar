package br.com.ocauamotta.PetLar.validations.Image;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface estratégica para a validação de arquivos de imagem enviados à plataforma.
 * <p>
 * Define o contrato para verificação de metadados, integridade e restrições técnicas
 * de arquivos carregados via formulários Multipart.
 */
public interface IImageValidation {
    void validate(MultipartFile image);
}
