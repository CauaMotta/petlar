package br.com.ocauamotta.PetLar.exceptions;

/**
 * Exceção lançada quando ocorre uma falha técnica ao tentar persistir um arquivo
 * de imagem no sistema de armazenamento.
 */
public class ImageNotSavedException extends RuntimeException {
    public ImageNotSavedException(String message) {
        super(message);
    }
}
