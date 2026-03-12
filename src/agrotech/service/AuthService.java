package agrotech.service;

import agrotech.client.AuthApiClient;
import agrotech.model.LoginResponse;
import agrotech.session.SessionManager;

public class AuthService {

    private final AuthApiClient authApiClient;

    public AuthService() {
        this.authApiClient = new AuthApiClient();
    }

    public LoginResponse login(String username, String password) {
        if (username == null || username.trim().isEmpty()|| 
            password == null || password.trim().isEmpty()) {
            return new LoginResponse(false, "Omple tots els camps.", null);
        }

        SessionManager.logout();
        LoginResponse response = authApiClient.login(username, password);

        if (response.isSuccess()) {
            SessionManager.login(response.getUser());
        }

        return response;
    }

    public void logout() {
        SessionManager.logout();
    }
}
