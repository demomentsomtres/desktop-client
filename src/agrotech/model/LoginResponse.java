package agrotech.model;

/**
 * Representa el resultat del procés de login.
 *
 * <p>
 * Inclou l'estat de l'operació, el missatge associat i, si el login és
 * correcte, l'usuari autenticat i el token de sessió.</p>
 *
 * @author Javier Santana Coronado
 */
public class LoginResponse {

    private boolean success;
    private String message;
    private User user;
    private String token;

    /**
     * Crea una resposta de login.
     *
     * @param success indica si el login ha estat correcte
     * @param message missatge associat al resultat
     * @param user usuari autenticat, si n'hi ha
     * @param token token de sessió retornat pel servidor, si n'hi ha
     */
    public LoginResponse(boolean success, String message, User user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }

    /**
     * Indica si el login ha estat correcte.
     *
     * @return {@code true} si el login ha estat correcte
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Retorna el missatge associat al resultat del login.
     *
     * @return missatge del procés
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retorna l'usuari autenticat.
     *
     * @return usuari autenticat o {@code null} si no n'hi ha
     */
    public User getUser() {
        return user;
    }

    /**
     * Retorna el token de sessió.
     *
     * @return token de sessió o {@code null} si no n'hi ha
     */
    public String getToken() {
        return token;
    }
}
