package agrotech.service;

import agrotech.client.AuthApiClient;
import agrotech.model.LoginResponse;
import agrotech.session.SessionManager;

public class AuthService {

    private final AuthApiClient authApiClient;

    public AuthService() {
        this.authApiClient = new AuthApiClient();
    }

    // Valida els camps bàsics, demana el login al servidor i guarda la sessió si és correcte
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

    // Envia el logout al servidor i neteja la sessió local si tot va bé
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
