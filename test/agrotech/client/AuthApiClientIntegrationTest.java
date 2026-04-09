package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.Role;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració de la classe AuthApiClient amb el servidor real.
 *
 * Es comprova el funcionament del login i del logout mitjançant comunicació
 * HTTP amb el servidor actiu.
 *
 * @author Javier Santana Coronado
 */
public class AuthApiClientIntegrationTest {

    private AuthApiClient authApiClient;

    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "user";

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Before
    public void setUp() {
        authApiClient = new AuthApiClient();
    }

    /**
     * Comprova que el login amb credencials incorrectes retorna error.
     */
    @Test
    public void login_credencialsErronies_retornaError() {
        LoginResponse response = authApiClient.login("usuari", "contrasenyaIncorrecta");

        assertFalse(response.isSuccess());
        assertNotNull(response.getMessage());
        assertNull(response.getUser());
        assertNull(response.getToken());
    }

    /**
     * Comprova que el login correcte d'un usuari estàndard retorna èxit, token
     * i rol CUSTOMER.
     */
    @Test
    public void login_usuariCorrecte_retornaTokenIRolClient() {
        LoginResponse response = authApiClient.login(USER_USERNAME, USER_PASSWORD);

        assertTrue(response.isSuccess());
        assertNotNull(response.getUser());
        assertNotNull(response.getToken());
        assertEquals(USER_USERNAME, response.getUser().getUsername());
        assertEquals(Role.CUSTOMER, response.getUser().getRole());
    }

    /**
     * Comprova que el login correcte d'un administrador retorna èxit, token i
     * rol ADMIN.
     */
    @Test
    public void login_adminCorrecte_retornaTokenIRolAdmin() {
        LoginResponse response = authApiClient.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        assertTrue(response.isSuccess());
        assertNotNull(response.getUser());
        assertNotNull(response.getToken());
        assertEquals(ADMIN_USERNAME, response.getUser().getUsername());
        assertEquals(Role.ADMIN, response.getUser().getRole());
    }

    /**
     * Comprova que el logout és correcte quan es fa amb un token vàlid.
     */
    @Test
    public void logout_ambTokenValid_retornaTrue() {
        LoginResponse response = authApiClient.login(USER_USERNAME, USER_PASSWORD);

        assertTrue(response.isSuccess());
        assertNotNull(response.getToken());

        boolean resultatLogout = authApiClient.logout(response.getToken());

        assertTrue(resultatLogout);
    }
}
