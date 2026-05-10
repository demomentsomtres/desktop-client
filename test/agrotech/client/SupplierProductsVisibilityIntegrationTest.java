package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.ManagedProduct;
import agrotech.model.ManagedSupplier;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració sobre la visibilitat de productes del rol SUPPLIER.
 *
 * <p>
 * Es comprova que un usuari amb rol proveïdor pot obtenir les seves pròpies
 * dades de proveïdor i que, en consultar els productes associats al seu
 * identificador, tots els productes retornats pertanyen realment a aquest
 * proveïdor.
 * </p>
 *
 * @author Javier Santana Coronado
 */
public class SupplierProductsVisibilityIntegrationTest {

    private AuthApiClient authApiClient;
    private SupplierApiClient supplierApiClient;
    private ProductApiClient productApiClient;

    private static final String SUPPLIER_USERNAME = "supplierJavier";
    private static final String SUPPLIER_PASSWORD = "1234";

    /**
     * Inicia sessió amb un usuari proveïdor real abans d'executar cada prova.
     */
    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        supplierApiClient = new SupplierApiClient();
        productApiClient = new ProductApiClient();

        LoginResponse loginResponse = authApiClient.login(
                SUPPLIER_USERNAME,
                SUPPLIER_PASSWORD
        );

        assertNotNull(
                "La resposta del login no hauria de ser null.",
                loginResponse
        );

        assertTrue(
                "El login del proveïdor hauria de funcionar. Comprova que el backend està en marxa.",
                loginResponse.isSuccess()
        );

        assertNotNull(
                "L'usuari autenticat no hauria de ser null.",
                loginResponse.getUser()
        );

        assertNotNull(
                "El token del proveïdor no hauria de ser null.",
                loginResponse.getToken()
        );

        assertEquals(
                "L'usuari autenticat hauria de tenir rol SUPPLIER.",
                Role.SUPPLIER,
                loginResponse.getUser().getRole()
        );

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());
    }

    /**
     * Tanca la sessió després de cada prova per evitar interferències entre
     * proves.
     */
    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que el proveïdor autenticat només recupera productes associats
     * al seu propi identificador de proveïdor.
     */
    @Test
    public void supplierAutenticat_nomésRecuperaElsSeusProductes() {
        ClientResponse<ManagedSupplier> supplierResponse
                = supplierApiClient.getMySupplier();

        assertNotNull(
                "La resposta de getMySupplier no hauria de ser null.",
                supplierResponse
        );

        assertTrue(
                "La consulta de les dades del proveïdor autenticat hauria de ser correcta.",
                supplierResponse.isSuccess()
        );

        ManagedSupplier supplier = supplierResponse.getData();

        assertNotNull(
                "El proveïdor autenticat no hauria de ser null.",
                supplier
        );

        assertNotNull(
                "El proveïdor autenticat hauria de tenir id.",
                supplier.getId()
        );

        assertNotNull(
                "El proveïdor autenticat hauria de tenir nom.",
                supplier.getName()
        );

        assertFalse(
                "El nom del proveïdor autenticat no hauria d'estar buit.",
                supplier.getName().trim().isEmpty()
        );

        ClientResponse<List<ManagedProduct>> productsResponse
                = productApiClient.getProductsBySupplierId(supplier.getId());

        assertNotNull(
                "La resposta de productes del proveïdor no hauria de ser null.",
                productsResponse
        );

        assertTrue(
                "La consulta de productes del proveïdor hauria de ser correcta.",
                productsResponse.isSuccess()
        );

        List<ManagedProduct> products = productsResponse.getData();

        assertNotNull(
                "La llista de productes del proveïdor no hauria de ser null.",
                products
        );

        assertFalse(
                "La llista de productes del proveïdor no hauria d'estar buida. "
                + "Si falla aquí, revisa que l'usuari supplier tingui productes associats a la base de dades.",
                products.isEmpty()
        );

        for (ManagedProduct product : products) {
            assertNotNull(
                    "Cada producte hauria de tenir id.",
                    product.getId()
            );

            assertNotNull(
                    "Cada producte hauria de tenir supplierId.",
                    product.getSupplierId()
            );

            assertEquals(
                    "El producte retornat hauria de pertànyer al proveïdor autenticat.",
                    supplier.getId(),
                    product.getSupplierId()
            );
        }
    }
}