package agrotech.dto.auth;

/**
 * Representa la resposta del backend al procés de login.
 *
 * <p>
 * Inclou l'estat de l'operació, el missatge retornat i l'usuari autenticat si
 * el login és correcte.</p>
 *
 * @author Javier Santana Coronado
 */
public class LoginResponseDto {

    private int status;
    private String message;
    private AuthenticatedUserDto user;

    /**
     * Retorna l'estat de la resposta.
     *
     * @return codi d'estat del backend
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

    /**
     * Retorna l'usuari autenticat.
     *
     * @return usuari autenticat o {@code null} si no n'hi ha
     */
    public AuthenticatedUserDto getUser() {
        return user;
    }
}
