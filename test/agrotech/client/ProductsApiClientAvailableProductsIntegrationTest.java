package agrotech.client;

import agrotech.dto.product.SaveProductRequestDto;
import agrotech.model.LoginResponse;
import agrotech.model.ManagedProduct;
import agrotech.model.ManagedSupplier;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració sobre la consulta de productes disponibles.
 *
 * <p>
 * Aquesta prova comprova que la consulta de productes disponibles retorna
 * productes que es poden comprar, és a dir, productes amb estoc i amb una data
 * de caducitat vàlida.
 * </p>
 *
 * <p>
 * Per evitar dependre de les dades inicials de la base de dades, la prova crea
 * un producte temporal amb caducitat futura i l'elimina en finalitzar.
 * </p>
 *
 * @author Javier Santana Coronado
 */
public class ProductsApiClientAvailableProductsIntegrationTest {

    private AuthApiClient authApiClient;
    private SupplierApiClient supplierApiClient;
    private ProductApiClient productApiClient;

    private Long createdProductId;

    private static final String SUPPLIER_USERNAME = "supplierJavier";
    private static final String SUPPLIER_PASSWORD = "1234";

    private static final String CUSTOMER_USERNAME = "userJavier";
    private static final String CUSTOMER_PASSWORD = "1234";

    private static final String TEMP_CATEGORY = "POMA";
    private static final Double TEMP_PRICE = 99.99;
    private static final Double TEMP_MARGIN = 7.0;
    private static final Double TEMP_TAX = 10.0;
    private static final Integer TEMP_STOCK = 9;
    private static final String TEMP_EXPIRATION_DATE = "2030-12-31";

    /**
     * Prepara els clients d'API i neteja qualsevol sessió anterior.
     */
    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        supplierApiClient = new SupplierApiClient();
        productApiClient = new ProductApiClient();
        createdProductId = null;
    }

    /**
     * Elimina el producte temporal creat durant la prova, si s'ha pogut crear.
     */
    @After
    public void tearDown() {
        if (createdProductId != null) {
            loginAs(SUPPLIER_USERNAME, SUPPLIER_PASSWORD, Role.SUPPLIER);
            productApiClient.deleteProduct(createdProductId);
        }

        SessionManager.logout();
    }

    /**
     * Comprova que la consulta de productes disponibles retorna productes amb
     * estoc i caducitat vàlida. També comprova que el producte temporal creat
     * apareix a la llista de productes disponibles.
     */
    @Test
    public void getAvailableProducts_retornaProductesAmbEstocICaducitatValida() {
        loginAs(SUPPLIER_USERNAME, SUPPLIER_PASSWORD, Role.SUPPLIER);

        ManagedSupplier supplier = getAuthenticatedSupplier();
        createTemporaryAvailableProduct();
        createdProductId = findTemporaryProductId(supplier.getId());

        assertNotNull(
                "No s'ha pogut localitzar el producte temporal creat.",
                createdProductId
        );

        loginAs(CUSTOMER_USERNAME, CUSTOMER_PASSWORD, Role.CUSTOMER);

        ClientResponse<List<ManagedProduct>> response
                = productApiClient.getAvailableProducts();

        assertNotNull(
                "La resposta de productes disponibles no hauria de ser null.",
                response
        );

        assertTrue(
                "La consulta de productes disponibles hauria de ser correcta.",
                response.isSuccess()
        );

        List<ManagedProduct> products = response.getData();

        assertNotNull(
                "La llista de productes disponibles no hauria de ser null.",
                products
        );

        assertFalse(
                "La llista de productes disponibles no hauria d'estar buida.",
                products.isEmpty()
        );

        assertTrue(
                "El producte temporal amb estoc i caducitat futura hauria d'aparèixer com a disponible.",
                containsProductWithId(products, createdProductId)
        );

        LocalDate today = LocalDate.now();

        for (ManagedProduct product : products) {
            assertNotNull(
                    "Cada producte disponible hauria de tenir id.",
                    product.getId()
            );

            assertNotNull(
                    "Cada producte disponible hauria de tenir estoc.",
                    product.getStock()
            );

            assertTrue(
                    "Cada producte disponible hauria de tenir estoc igual o superior a 1.",
                    product.getStock() >= 1
            );

            assertNotNull(
                    "Cada producte disponible hauria de tenir data de caducitat.",
                    product.getExpirationDate()
            );

            LocalDate expirationDate = parseExpirationDate(product.getExpirationDate());

            assertFalse(
                    "Cada producte disponible hauria de tenir una caducitat igual o posterior a avui.",
                    expirationDate.isBefore(today)
            );
        }
    }

    /**
     * Inicia sessió amb l'usuari indicat i desa el token al SessionManager.
     *
     * @param username nom d'usuari
     * @param password contrasenya
     * @param expectedRole rol esperat
     */
    private void loginAs(String username, String password, Role expectedRole) {
        SessionManager.logout();

        LoginResponse loginResponse = authApiClient.login(username, password);

        assertNotNull(
                "La resposta del login no hauria de ser null.",
                loginResponse
        );

        assertTrue(
                "El login hauria de ser correcte per a l'usuari " + username + ".",
                loginResponse.isSuccess()
        );

        assertNotNull(
                "L'usuari autenticat no hauria de ser null.",
                loginResponse.getUser()
        );

        assertNotNull(
                "El token no hauria de ser null.",
                loginResponse.getToken()
        );

        assertEquals(
                "El rol de l'usuari autenticat no és l'esperat.",
                expectedRole,
                loginResponse.getUser().getRole()
        );

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());
    }

    /**
     * Recupera les dades del proveïdor autenticat.
     *
     * @return proveïdor autenticat
     */
    private ManagedSupplier getAuthenticatedSupplier() {
        ClientResponse<ManagedSupplier> response = supplierApiClient.getMySupplier();

        assertNotNull(
                "La resposta de getMySupplier no hauria de ser null.",
                response
        );

        assertTrue(
                "La consulta del proveïdor autenticat hauria de ser correcta.",
                response.isSuccess()
        );

        assertNotNull(
                "El proveïdor autenticat no hauria de ser null.",
                response.getData()
        );

        assertNotNull(
                "El proveïdor autenticat hauria de tenir id.",
                response.getData().getId()
        );

        return response.getData();
    }

    /**
     * Crea un producte temporal disponible per a la compra.
     */
    private void createTemporaryAvailableProduct() {
        SaveProductRequestDto request = new SaveProductRequestDto(
                TEMP_CATEGORY,
                TEMP_PRICE,
                TEMP_MARGIN,
                TEMP_TAX,
                TEMP_EXPIRATION_DATE,
                TEMP_STOCK
        );

        ClientResponse<Void> response = productApiClient.createProduct(request);

        assertNotNull(
                "La resposta de creació del producte temporal no hauria de ser null.",
                response
        );

        assertTrue(
                "El producte temporal s'hauria de crear correctament.",
                response.isSuccess()
        );
    }

    /**
     * Localitza l'identificador del producte temporal creat.
     *
     * @param supplierId identificador del proveïdor autenticat
     * @return id del producte temporal, o null si no es troba
     */
    private Long findTemporaryProductId(Long supplierId) {
        ClientResponse<List<ManagedProduct>> response
                = productApiClient.getProductsBySupplierId(supplierId);

        assertNotNull(
                "La resposta de productes del proveïdor no hauria de ser null.",
                response
        );

        assertTrue(
                "La consulta de productes del proveïdor hauria de ser correcta.",
                response.isSuccess()
        );

        List<ManagedProduct> products = response.getData();

        assertNotNull(
                "La llista de productes del proveïdor no hauria de ser null.",
                products
        );

        return products.stream()
                .filter(this::isTemporaryProduct)
                .max(Comparator.comparing(ManagedProduct::getId))
                .map(ManagedProduct::getId)
                .orElse(null);
    }

    /**
     * Comprova si un producte coincideix amb el producte temporal de la prova.
     *
     * @param product producte a comprovar
     * @return true si coincideix amb el producte temporal
     */
    private boolean isTemporaryProduct(ManagedProduct product) {
        if (product == null) {
            return false;
        }

        return TEMP_CATEGORY.equals(product.getProductCategory())
                && TEMP_STOCK.equals(product.getStock())
                && TEMP_EXPIRATION_DATE.equals(product.getExpirationDate())
                && product.getPrice() != null
                && Math.abs(product.getPrice() - TEMP_PRICE) < 0.001
                && product.getCooperativeMargin() != null
                && Math.abs(product.getCooperativeMargin() - TEMP_MARGIN) < 0.001
                && product.getTax() != null
                && Math.abs(product.getTax() - TEMP_TAX) < 0.001;
    }

    /**
     * Comprova si una llista conté un producte amb l'identificador indicat.
     *
     * @param products llista de productes
     * @param productId identificador del producte
     * @return true si existeix un producte amb aquest identificador
     */
    private boolean containsProductWithId(List<ManagedProduct> products, Long productId) {
        return products.stream()
                .anyMatch(product -> productId.equals(product.getId()));
    }

    /**
     * Converteix la data de caducitat rebuda del backend a LocalDate.
     *
     * @param expirationDate data de caducitat en format text
     * @return data convertida
     */
    private LocalDate parseExpirationDate(String expirationDate) {
        String normalizedDate = expirationDate;

        if (expirationDate.length() > 10) {
            normalizedDate = expirationDate.substring(0, 10);
        }

        return LocalDate.parse(normalizedDate);
    }
}
