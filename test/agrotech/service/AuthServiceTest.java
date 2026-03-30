package agrotech.service;

import agrotech.model.LoginResponse;
import agrotech.model.Role;
import agrotech.model.User;
import agrotech.session.SessionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves unitàries bàsiques de la classe AuthService.
 *
 * Es comprova la validació de camps i el comportament del logout
 * quan no hi ha token actiu.
 *
 * @author Javier Santana Coronado
 */
public class AuthServiceTest {

    private AuthService authService;

    @Before
    public void setUp() {
        SessionManager.logout();
        authService = new AuthService();
    }

    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que el login falla si el nom d'usuari és buit.
     */
    @Test
    public void login_usuariBuit_retornErrorValidacio() {
        LoginResponse response = authService.login("", "1234");

        assertFalse(response.isSuccess());
        assertEquals("Omple tots els camps.", response.getMessage());
        assertNull(response.getUser());
        assertNull(response.getToken());
    }

    /**
     * Comprova que el login falla si la contrasenya és buida.
     */
    @Test
    public void login_contrasenyaBuida_retornErrorValidacio() {
        LoginResponse response = authService.login("javier", "");

        assertFalse(response.isSuccess());
        assertEquals("Omple tots els camps.", response.getMessage());
        assertNull(response.getUser());
        assertNull(response.getToken());
    }

    /**
     * Comprova que el logout retorna true i neteja la sessió
     * quan no hi ha token actiu.
     */
    @Test
    public void logout_senseToken_retornTrueINetejaSessio() {
        User user = new User(1, "javier", Role.CLIENT);
        SessionManager.login(user, null);

        boolean resultat = authService.logout();

        assertTrue(resultat);
        assertNull(SessionManager.getCurrentUser());
        assertNull(SessionManager.getCurrentToken());
    }
}