package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.Role;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Prova d'integració del login amb un usuari proveïdor real.
 *
 * Es comprova que el backend autentica correctament l'usuari i que el rol
 * retornat és SUPPLIER.
 *
 * @author Javier Santana Coronado
 */
public class AuthApiClientSupplierIntegrationTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private static final String SUPPLIER_USERNAME = "supplier";
    private static final String SUPPLIER_PASSWORD = "supplier";

    /**
     * Comprova que el login d'un proveïdor retorna èxit, usuari, token i rol
     * SUPPLIER.
     */
    @Test
    public void loginSupplierCorrecte_retornaRolSupplier() {
        LoginResponse response = authApiClient.login(SUPPLIER_USERNAME, SUPPLIER_PASSWORD);

        assertNotNull("La resposta del login no hauria de ser null.", response);
        assertTrue("El login del proveïdor hauria de ser correcte.", response.isSuccess());

        assertNotNull("L'usuari retornat no hauria de ser null.", response.getUser());
        assertNotNull("El token retornat no hauria de ser null.", response.getToken());

        assertEquals("El rol retornat hauria de ser SUPPLIER.",
                Role.SUPPLIER, response.getUser().getRole());
    }
}
