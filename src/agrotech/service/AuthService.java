package agrotech.service;

import agrotech.client.AuthApiClient;
import agrotech.model.LoginResponse;
import agrotech.session.SessionManager;

/**
 * Servei encarregat de gestionar la lògica d'autenticació del client
 * escriptori.
 *
 * <p>
 * Valida les dades d'entrada, delega la comunicació HTTP a
 * {@link AuthApiClient} i actualitza la sessió local mitjançant
 * {@link SessionManager}.</p>
 *
 * @author Javier Santana Coronado
 */
public class AuthService {

    private final AuthApiClient authApiClient;

    /**
     * Crea el servei d'autenticació del client.
     */
    public AuthService() {
        this.authApiClient = new AuthApiClient();
    }

    /**
     * Intenta iniciar sessió amb les credencials indicades.
     *
     * <p>
     * Si els camps són buits, retorna un error de validació. Si el servidor
     * valida correctament l'usuari, desa la sessió local.</p>
     *
     * @param username nom d'usuari introduït al formulari
     * @param password contrasenya introduïda al formulari
     * @return resultat del procés de login
     */
    public LoginResponse login(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return new LoginResponse(false, "Omple tots els camps.", null, null);
        }

        SessionManager.logout();
        LoginResponse response = authApiClient.login(username, password);

        if (response.isSuccess()) {
            SessionManager.login(response.getUser(), response.getToken());
        }

        return response;
    }

    /**
     * Intenta tancar la sessió actual.
     *
     * <p>
     * Si no hi ha token, neteja igualment la sessió local. Si hi ha token,
     * envia la petició de logout al servidor i només elimina la sessió local si
     * el procés finalitza correctament.</p>
     *
     * @return {@code true} si el logout s'ha completat correctament;
     * {@code false} en cas contrari
     */
    public boolean logout() {
        String token = SessionManager.getCurrentToken();

        if (token == null || token.isBlank()) {
            SessionManager.logout();
            return true;
        }

        boolean logoutOk = authApiClient.logout(token);

        if (logoutOk) {
            SessionManager.logout();
        }

        return logoutOk;
    }
}
