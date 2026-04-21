package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.ManagedOrder;
import agrotech.session.SessionManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració de la classe OrderApiClient amb el servidor real.
 *
 * Es comprova que un usuari customer autenticat pot recuperar correctament les
 * seves comandes des del backend.
 *
 * @author Javier Santana Coronado
 */
public class OrdersApiClientIntegrationTest {

    private AuthApiClient authApiClient;
    private OrderApiClient orderApiClient;

    private static final String CUSTOMER_USERNAME = "user";
    private static final String CUSTOMER_PASSWORD = "user";

    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        orderApiClient = new OrderApiClient();

        LoginResponse loginResponse = authApiClient.login(CUSTOMER_USERNAME, CUSTOMER_PASSWORD);

        assertTrue(
                "No s'ha pogut fer el login previ del customer. Comprova que el backend està en marxa i accessible.",
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
     * Comprova que un customer pot recuperar correctament la seva llista de
     * comandes des del backend.
     */
    @Test
    public void getMyOrders_retornaComandesCorrectament() {
        ClientResponse<List<ManagedOrder>> response = orderApiClient.getMyOrders();

        assertNotNull("La resposta no hauria de ser null.", response);
        assertTrue("La consulta de comandes hauria de ser correcta.", response.isSuccess());
        assertNotNull("La llista de comandes no hauria de ser null.", response.getData());
        assertFalse("La llista de comandes no hauria d'estar buida.", response.getData().isEmpty());

        ManagedOrder primeraComanda = response.getData().get(0);
        assertNotNull("La comanda hauria de tenir id.", primeraComanda.getId());
        assertNotNull("La comanda hauria de tenir població de destí.", primeraComanda.getPoblacioDesti());
    }
}
