package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.ManagedUser;
import agrotech.session.SessionManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració de la classe UserApiClient amb el servidor real.
 *
 * Es comprova que l'aplicació client pot recuperar correctament la llista
 * d'usuaris des del backend amb una sessió d'administrador vàlida.
 *
 * @author Javier Santana Coronado
 */
public class UsersApiClientIntegrationTest {

    private AuthApiClient authApiClient;
    private UserApiClient userApiClient;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        userApiClient = new UserApiClient();

        LoginResponse loginResponse = authApiClient.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        assertTrue(
                "No s'ha pogut fer el login previ. Comprova que el backend està en marxa i accessible.",
                loginResponse.isSuccess()
        );
        assertNotNull("L'usuari autenticat no pot ser null.", loginResponse.getUser());
        assertNotNull("El token no pot ser null.", loginResponse.getToken());

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());
    }

    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que la consulta de tots els usuaris retorna èxit i una llista
     * vàlida des del backend.
     */
    @Test
    public void getAllUsers_retornaUsuarisCorrectament() {
        ClientResponse<List<ManagedUser>> response = userApiClient.getAllUsers();

        assertNotNull("La resposta no hauria de ser null.", response);
        assertTrue("La consulta d'usuaris hauria de ser correcta.", response.isSuccess());
        assertNotNull("La llista d'usuaris no hauria de ser null.", response.getData());
        assertFalse("La llista d'usuaris no hauria d'estar buida.", response.getData().isEmpty());

        ManagedUser primerUsuari = response.getData().get(0);
        assertNotNull("L'usuari hauria de tenir username.", primerUsuari.getUsername());
        assertFalse("El username no hauria d'estar buit.", primerUsuari.getUsername().trim().isEmpty());
        assertNotNull("L'usuari hauria de tenir rol.", primerUsuari.getRole());
    }
}
