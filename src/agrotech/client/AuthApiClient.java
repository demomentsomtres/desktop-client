package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.Role;
import agrotech.model.User;

public class AuthApiClient {

    public LoginResponse login(String username, String password) {
        // TODO: substituir aquesta simulació per una crida real al servidor

        if ("client".equals(username) && "1234".equals(password)) {
            User user = new User(1, "client", Role.CLIENT);
            return new LoginResponse(true, "Login correcte.", user);
        }

        return new LoginResponse(false, "Usuari o contrasenya incorrectes.", null);
    }
}
