package agrotech.session;

import agrotech.model.User;

// Classe estàtica que guarda la sessió actual del client (usuari i token JWT)
public class SessionManager {

    private static User currentUser;
    private static String currentToken;

    public static void login(User user, String token) {
        currentUser = user;
        currentToken = token;
    }

    public static void logout() {
        currentUser = null;
        currentToken = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentToken() {
        return currentToken;
    }

    public static boolean isLoggedIn() {
        return currentUser != null && currentToken != null;
    }
}
