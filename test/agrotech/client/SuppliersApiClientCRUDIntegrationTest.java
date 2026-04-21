package agrotech.client;

import agrotech.dto.supplier.SaveSupplierRequestDto;
import agrotech.model.LoginResponse;
import agrotech.model.ManagedSupplier;
import agrotech.session.SessionManager;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració CRUD de la classe SupplierApiClient amb el servidor real.
 *
 * Es comprova que l'aplicació client pot crear, modificar i eliminar proveïdors
 * correctament des del backend amb una sessió d'administrador vàlida.
 *
 * @author Javier Santana Coronado
 */
public class SuppliersApiClientCRUDIntegrationTest {

    private AuthApiClient authApiClient;
    private SupplierApiClient supplierApiClient;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String TEST_SUPPLIER_NAME = "supplierTest_CRUD";
    private static final String TEST_SUPPLIER_PASSWORD = "test1234";
    private static final String TEST_SUPPLIER_POBLACIO = "TestPoblacio";
    private static final String TEST_SUPPLIER_EMAIL = "suppliertest_crud@test.com";
    private static final String TEST_SUPPLIER_TELEFON = "600000099";

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

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());

        supplierApiClient.deleteSupplier(TEST_SUPPLIER_NAME);
        supplierApiClient.deleteSupplier(TEST_SUPPLIER_NAME + "_modificat");
    }

    @After
    public void tearDown() {
        supplierApiClient.deleteSupplier(TEST_SUPPLIER_NAME);
        supplierApiClient.deleteSupplier(TEST_SUPPLIER_NAME + "_modificat");
        SessionManager.logout();
    }

    /**
     * Comprova que es pot crear un proveïdor nou correctament.
     */
    @Test
    public void createSupplier_creaCorrectament() {
        SaveSupplierRequestDto request = new SaveSupplierRequestDto(
                TEST_SUPPLIER_NAME,
                TEST_SUPPLIER_PASSWORD,
                TEST_SUPPLIER_POBLACIO,
                TEST_SUPPLIER_EMAIL,
                TEST_SUPPLIER_TELEFON
        );

        ClientResponse<Void> response = supplierApiClient.createSupplier(request);

        assertTrue("La creació del proveïdor hauria de ser correcta.", response.isSuccess());

        ClientResponse<ManagedSupplier> verificacio
                = supplierApiClient.getSupplierByName(TEST_SUPPLIER_NAME);
        assertTrue("El proveïdor creat hauria de ser consultable.", verificacio.isSuccess());
        assertEquals("El nom del proveïdor hauria de coincidir.",
                TEST_SUPPLIER_NAME, verificacio.getData().getName());
    }

    /**
     * Comprova que es pot modificar un proveïdor existent correctament.
     */
    @Test
    public void updateSupplier_modificaCorrectament() {
        SaveSupplierRequestDto createRequest = new SaveSupplierRequestDto(
                TEST_SUPPLIER_NAME,
                TEST_SUPPLIER_PASSWORD,
                TEST_SUPPLIER_POBLACIO,
                TEST_SUPPLIER_EMAIL,
                TEST_SUPPLIER_TELEFON
        );
        supplierApiClient.createSupplier(createRequest);

        SaveSupplierRequestDto updateRequest = new SaveSupplierRequestDto(
                TEST_SUPPLIER_NAME + "_modificat",
                null,
                "PoblacioModificada",
                "modificat_crud@test.com",
                "600000098"
        );

        ClientResponse<Void> response
                = supplierApiClient.updateSupplier(TEST_SUPPLIER_NAME, updateRequest);

        assertTrue("La modificació del proveïdor hauria de ser correcta.", response.isSuccess());

        ClientResponse<ManagedSupplier> verificacio
                = supplierApiClient.getSupplierByName(TEST_SUPPLIER_NAME + "_modificat");
        assertTrue("El proveïdor modificat hauria de ser consultable.", verificacio.isSuccess());
        assertEquals("La població hauria d'haver canviat.",
                "PoblacioModificada", verificacio.getData().getPoblacio());
    }

    /**
     * Comprova que es pot eliminar un proveïdor existent correctament.
     */
    @Test
    public void deleteSupplier_eliminaCorrectament() {
        SaveSupplierRequestDto createRequest = new SaveSupplierRequestDto(
                TEST_SUPPLIER_NAME,
                TEST_SUPPLIER_PASSWORD,
                TEST_SUPPLIER_POBLACIO,
                TEST_SUPPLIER_EMAIL,
                TEST_SUPPLIER_TELEFON
        );
        supplierApiClient.createSupplier(createRequest);

        ClientResponse<Void> response = supplierApiClient.deleteSupplier(TEST_SUPPLIER_NAME);

        assertTrue("L'eliminació del proveïdor hauria de ser correcta.", response.isSuccess());

        ClientResponse<List<ManagedSupplier>> verificacio
                = supplierApiClient.getAllSuppliers();
        assertTrue("La consulta hauria de ser correcta.", verificacio.isSuccess());

        boolean existeix = verificacio.getData().stream()
                .anyMatch(s -> TEST_SUPPLIER_NAME.equals(s.getName()));
        assertFalse("El proveïdor eliminat no hauria d'apareixer a la llista.", existeix);
    }
}
