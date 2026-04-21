package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.ManagedSupplier;
import agrotech.session.SessionManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració de la classe SupplierApiClient amb el servidor real.
 *
 * Es comprova que l'aplicació client pot recuperar correctament la llista de
 * proveïdors des del backend amb una sessió vàlida.
 *
 * @author Javier Santana Coronado
 */
public class SuppliersApiClientIntegrationTest {

    private AuthApiClient authApiClient;
    private SupplierApiClient supplierApiClient;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        supplierApiClient = new SupplierApiClient();

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
     * Comprova que la consulta de tots els proveïdors retorna èxit i una llista
     * vàlida des del backend.
     */
    @Test
    public void getAllSuppliers_retornaProveidorsCorrectament() {
        ClientResponse<List<ManagedSupplier>> response = supplierApiClient.getAllSuppliers();

        assertNotNull("La resposta no hauria de ser null.", response);
        assertTrue("La consulta de proveïdors hauria de ser correcta.", response.isSuccess());
        assertNotNull("La llista de proveïdors no hauria de ser null.", response.getData());
        assertFalse("La llista de proveïdors no hauria d'estar buida.", response.getData().isEmpty());

        ManagedSupplier primerProveidor = response.getData().get(0);
        assertNotNull("El proveïdor hauria de tenir nom.", primerProveidor.getName());
        assertFalse("El nom del proveïdor no hauria d'estar buit.", primerProveidor.getName().trim().isEmpty());
    }
}
