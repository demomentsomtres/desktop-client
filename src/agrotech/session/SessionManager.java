package agrotech.session;

import agrotech.model.User;

/**
 * Classe estàtica que gestiona la sessió actual del client escriptori.
 *
 * <p>
 * Desa temporalment l'usuari autenticat i el token de sessió mentre l'aplicació
 * està en execució.</p>
 *
 * @author Javier Santana Coronado
 */
public class SessionManager {

    private static User currentUser;
    private static String currentToken;

    /**
     * Desa la informació de la sessió actual.
     *
     * @param user usuari autenticat
     * @param token token retornat pel servidor
     */
    public static void login(User user, String token) {
        currentUser = user;
        currentToken = token;
    }

    /**
     * Elimina la sessió actual del client.
     */
    public static void logout() {
        currentUser = null;
        currentToken = null;
    }

    /**
     * Retorna l'usuari autenticat actual.
     *
     * @return usuari actual o {@code null} si no hi ha sessió
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Retorna el token de la sessió actual.
     *
     * @return token actual o {@code null} si no hi ha sessió
     */
    public static String getCurrentToken() {
        return currentToken;
    }
}
