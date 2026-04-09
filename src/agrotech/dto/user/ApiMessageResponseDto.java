package agrotech.dto.user;

/**
 * Representa una resposta simple del backend.
 *
 * <p>
 * Inclou l'estat de la petició i el missatge retornat.</p>
 *
 * @author Javier Santana Coronado
 */
public class ApiMessageResponseDto {

    private int status;
    private String message;

    /**
     * Retorna l'estat de la resposta.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
    }

    /**
     * Retorna el missatge de la resposta.
     *
     * @return missatge retornat pel backend
     */
    public String getMessage() {
        return message;
    }
}
