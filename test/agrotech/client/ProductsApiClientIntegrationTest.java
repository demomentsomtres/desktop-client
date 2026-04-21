package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.session.SessionManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració de la classe ProductApiClient amb el servidor real.
 *
 * Es comprova que l'aplicació client pot recuperar correctament els productes i
 * les categories des del backend amb una sessió vàlida.
 *
 * @author Javier Santana Coronado
 */
public class ProductsApiClientIntegrationTest {

    private AuthApiClient authApiClient;
    private ProductApiClient productApiClient;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        productApiClient = new ProductApiClient();

        LoginResponse loginResponse = authApiClient.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        assertTrue("El login previ ha de funcionar per executar la prova d'integració.",
                loginResponse.isSuccess());
        assertNotNull("L'usuari autenticat no pot ser null.", loginResponse.getUser());
        assertNotNull("El token no pot ser null.", loginResponse.getToken());

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());
    }

    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que la consulta de tots els productes retorna èxit i una llista
     * vàlida des del backend.
     */
    @Test
    public void getAllProducts_retornaProductesCorrectament() {
        ClientResponse<List<agrotech.model.ManagedProduct>> response = productApiClient.getAllProducts();

        assertTrue("La consulta de productes hauria de ser correcta.", response.isSuccess());
        assertNotNull("La resposta no hauria de ser null.", response);
        assertNotNull("La llista de productes no hauria de ser null.", response.getData());

        assertFalse("La llista de productes no hauria d'estar buida.",
                response.getData().isEmpty());

        assertNotNull("El primer producte hauria de tenir id.",
                response.getData().get(0).getId());

        assertNotNull("El primer producte hauria de tenir categoria.",
                response.getData().get(0).getProductCategory());
    }

    /**
     * Comprova que la consulta de categories retorna èxit i una llista vàlida
     * des del backend.
     */
    @Test
    public void getAllCategories_retornaCategoriesCorrectament() {
        ClientResponse<List<String>> response = productApiClient.getAllCategories();

        assertTrue("La consulta de categories hauria de ser correcta.", response.isSuccess());
        assertNotNull("La resposta no hauria de ser null.", response);
        assertNotNull("La llista de categories no hauria de ser null.", response.getData());

        assertFalse("La llista de categories no hauria d'estar buida.",
                response.getData().isEmpty());

        assertNotNull("La primera categoria no hauria de ser null.",
                response.getData().get(0));

        assertFalse("La primera categoria no hauria d'estar buida.",
                response.getData().get(0).trim().isEmpty());
    }
}
