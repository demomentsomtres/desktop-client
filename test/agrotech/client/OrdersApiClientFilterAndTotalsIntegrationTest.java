package agrotech.client;

import agrotech.dto.order.OrderLineDto;
import agrotech.model.LoginResponse;
import agrotech.model.ManagedOrder;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració sobre la consulta filtrada de comandes.
 *
 * <p>
 * Aquesta prova comprova que un usuari administrador pot consultar comandes
 * aplicant filtres i que les dades econòmiques retornades pel backend arriben
 * correctament al model del client.
 * </p>
 *
 * @author Javier Santana Coronado
 */
public class OrdersApiClientFilterAndTotalsIntegrationTest {

    private AuthApiClient authApiClient;
    private OrderApiClient orderApiClient;

    private static final String ADMIN_USERNAME = "javier";
    private static final String ADMIN_PASSWORD = "1234";

    private static final double RANGE_MARGIN = 0.01;

    /**
     * Inicia sessió com a administrador abans d'executar cada prova.
     */
    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        orderApiClient = new OrderApiClient();

        loginAsAdmin();
    }

    /**
     * Tanca la sessió després de cada prova per evitar interferències.
     */
    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que la consulta filtrada de comandes retorna resultats coherents
     * i que els camps econòmics principals no es perden en el mapatge del
     * backend cap al client.
     */
    @Test
    public void getOrdersByFilters_retornaComandesFiltradesAmbTotalsCoherents() {
        ClientResponse<List<ManagedOrder>> allOrdersResponse
                = orderApiClient.getOrdersByFilters(
                        null, null,
                        null, null,
                        null, null,
                        null, null,
                        null, null
                );

        assertNotNull(
                "La resposta de consulta de comandes no hauria de ser null.",
                allOrdersResponse
        );

        assertTrue(
                "La consulta inicial de comandes hauria de ser correcta.",
                allOrdersResponse.isSuccess()
        );

        List<ManagedOrder> allOrders = allOrdersResponse.getData();

        assertNotNull(
                "La llista de comandes no hauria de ser null.",
                allOrders
        );

        assertFalse(
                "La base de dades hauria de tenir com a mínim una comanda per poder provar els filtres.",
                allOrders.isEmpty()
        );

        ManagedOrder referenceOrder = findOrderWithRequiredData(allOrders);

        assertNotNull(
                "No s'ha trobat cap comanda amb les dades mínimes necessàries per aplicar filtres.",
                referenceOrder
        );

        assertOrderBasicDataIsValid(referenceOrder);
        assertOrderEconomicDataIsValid(referenceOrder);
        assertOrderLinesAreValid(referenceOrder);

        Long customerId = Long.valueOf(referenceOrder.getCustomerId());
        String poblacioFilter = "%" + referenceOrder.getPoblacioDesti() + "%";
        String orderDate = normalizeDate(referenceOrder.getDate());

        Double minTotal = referenceOrder.getTotalOrderPrice() - RANGE_MARGIN;
        Double maxTotal = referenceOrder.getTotalOrderPrice() + RANGE_MARGIN;

        Double minMargin = referenceOrder.getTotalOrderCooperativeMargin() - RANGE_MARGIN;
        Double maxMargin = referenceOrder.getTotalOrderCooperativeMargin() + RANGE_MARGIN;

        Double minTax = referenceOrder.getTotalOrderTax() - RANGE_MARGIN;
        Double maxTax = referenceOrder.getTotalOrderTax() + RANGE_MARGIN;

        ClientResponse<List<ManagedOrder>> filteredResponse
                = orderApiClient.getOrdersByFilters(
                        customerId,
                        poblacioFilter,
                        orderDate,
                        orderDate,
                        minTotal,
                        maxTotal,
                        minMargin,
                        maxMargin,
                        minTax,
                        maxTax
                );

        assertNotNull(
                "La resposta de consulta filtrada no hauria de ser null.",
                filteredResponse
        );

        assertTrue(
                "La consulta filtrada de comandes hauria de ser correcta.",
                filteredResponse.isSuccess()
        );

        List<ManagedOrder> filteredOrders = filteredResponse.getData();

        assertNotNull(
                "La llista de comandes filtrades no hauria de ser null.",
                filteredOrders
        );

        assertFalse(
                "La consulta filtrada hauria de retornar com a mínim la comanda de referència.",
                filteredOrders.isEmpty()
        );

        assertTrue(
                "La consulta filtrada hauria d'incloure la comanda utilitzada com a referència.",
                containsOrderWithId(filteredOrders, referenceOrder.getId())
        );

        for (ManagedOrder order : filteredOrders) {
            assertOrderBasicDataIsValid(order);
            assertOrderEconomicDataIsValid(order);
            assertOrderLinesAreValid(order);

            assertEquals(
                    "La comanda filtrada hauria de pertànyer al client indicat.",
                    customerId.toString(),
                    order.getCustomerId()
            );

            assertEquals(
                    "La comanda filtrada hauria de tenir la data indicada.",
                    orderDate,
                    normalizeDate(order.getDate())
            );

            assertTrue(
                    "La població de la comanda hauria de coincidir amb el filtre aplicat.",
                    order.getPoblacioDesti()
                            .toLowerCase()
                            .contains(referenceOrder.getPoblacioDesti().toLowerCase())
            );

            assertTrue(
                    "El total de la comanda hauria d'estar dins del rang filtrat.",
                    isBetween(order.getTotalOrderPrice(), minTotal, maxTotal)
            );

            assertTrue(
                    "El marge cooperatiu hauria d'estar dins del rang filtrat.",
                    isBetween(order.getTotalOrderCooperativeMargin(), minMargin, maxMargin)
            );

            assertTrue(
                    "L'IVA hauria d'estar dins del rang filtrat.",
                    isBetween(order.getTotalOrderTax(), minTax, maxTax)
            );
        }
    }

    /**
     * Inicia sessió com a administrador i desa el token al SessionManager.
     */
    private void loginAsAdmin() {
        LoginResponse loginResponse = authApiClient.login(
                ADMIN_USERNAME,
                ADMIN_PASSWORD
        );

        assertNotNull(
                "La resposta del login no hauria de ser null.",
                loginResponse
        );

        assertTrue(
                "El login d'administrador hauria de funcionar. Comprova que el backend està en marxa.",
                loginResponse.isSuccess()
        );

        assertNotNull(
                "L'usuari autenticat no hauria de ser null.",
                loginResponse.getUser()
        );

        assertNotNull(
                "El token de l'administrador no hauria de ser null.",
                loginResponse.getToken()
        );

        assertEquals(
                "L'usuari autenticat hauria de tenir rol ADMIN.",
                Role.ADMIN,
                loginResponse.getUser().getRole()
        );

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());
    }

    /**
     * Busca una comanda que tingui les dades necessàries per construir una
     * consulta filtrada fiable.
     *
     * @param orders llista de comandes
     * @return comanda vàlida o null si no se'n troba cap
     */
    private ManagedOrder findOrderWithRequiredData(List<ManagedOrder> orders) {
        for (ManagedOrder order : orders) {
            if (order == null) {
                continue;
            }

            if (order.getId() != null
                    && hasText(order.getCustomerId())
                    && hasText(order.getDate())
                    && hasText(order.getPoblacioDesti())
                    && order.getTotalOrderPrice() != null
                    && order.getTotalOrderCooperativeMargin() != null
                    && order.getTotalOrderTax() != null
                    && order.getTotalDiscount() != null
                    && order.getOrderLines() != null
                    && !order.getOrderLines().isEmpty()) {
                return order;
            }
        }

        return null;
    }

    /**
     * Comprova les dades bàsiques d'una comanda.
     *
     * @param order comanda a comprovar
     */
    private void assertOrderBasicDataIsValid(ManagedOrder order) {
        assertNotNull(
                "La comanda no hauria de ser null.",
                order
        );

        assertNotNull(
                "La comanda hauria de tenir id.",
                order.getId()
        );

        assertTrue(
                "La comanda hauria de tenir customerId.",
                hasText(order.getCustomerId())
        );

        assertTrue(
                "La comanda hauria de tenir data.",
                hasText(order.getDate())
        );

        assertTrue(
                "La comanda hauria de tenir població de destí.",
                hasText(order.getPoblacioDesti())
        );

        LocalDate.parse(normalizeDate(order.getDate()));
    }

    /**
     * Comprova que els camps econòmics principals de la comanda són coherents.
     *
     * @param order comanda a comprovar
     */
    private void assertOrderEconomicDataIsValid(ManagedOrder order) {
        assertNotNull(
                "El total de la comanda no hauria de ser null.",
                order.getTotalOrderPrice()
        );

        assertNotNull(
                "El marge cooperatiu total no hauria de ser null.",
                order.getTotalOrderCooperativeMargin()
        );

        assertNotNull(
                "L'IVA total de la comanda no hauria de ser null.",
                order.getTotalOrderTax()
        );

        assertNotNull(
                "El descompte total de la comanda no hauria de ser null.",
                order.getTotalDiscount()
        );

        assertTrue(
                "El total de la comanda hauria de ser igual o superior a 0.",
                order.getTotalOrderPrice() >= 0
        );

        assertTrue(
                "El marge cooperatiu total hauria de ser igual o superior a 0.",
                order.getTotalOrderCooperativeMargin() >= 0
        );

        assertTrue(
                "L'IVA total hauria de ser igual o superior a 0.",
                order.getTotalOrderTax() >= 0
        );

        assertTrue(
                "El descompte total hauria de ser igual o superior a 0.",
                order.getTotalDiscount() >= 0
        );
    }

    /**
     * Comprova que les línies d'una comanda tenen dades i totals coherents.
     *
     * @param order comanda amb línies
     */
    private void assertOrderLinesAreValid(ManagedOrder order) {
        List<OrderLineDto> lines = order.getOrderLines();

        assertNotNull(
                "Les línies de la comanda no haurien de ser null.",
                lines
        );

        assertFalse(
                "La comanda hauria de tenir com a mínim una línia.",
                lines.isEmpty()
        );

        for (OrderLineDto line : lines) {
            assertNotNull(
                    "Cada línia hauria de tenir id.",
                    line.getId()
            );

            assertTrue(
                    "Cada línia hauria de tenir categoria de producte.",
                    hasText(line.getProductCategory())
            );

            assertNotNull(
                    "Cada línia hauria de tenir quantitat.",
                    line.getQuantity()
            );

            assertTrue(
                    "La quantitat de cada línia hauria de ser superior a 0.",
                    line.getQuantity() > 0
            );

            assertNotNull(
                    "Cada línia hauria de tenir preu unitari.",
                    line.getUnitPrice()
            );

            assertNotNull(
                    "Cada línia hauria de tenir total de preu.",
                    line.getTotalPrice()
            );

            assertNotNull(
                    "Cada línia hauria de tenir total d'IVA.",
                    line.getTotalTax()
            );

            assertNotNull(
                    "Cada línia hauria de tenir marge cooperatiu.",
                    line.getTotalCooperativeMargin()
            );

            assertNotNull(
                    "Cada línia hauria de tenir descompte total.",
                    line.getTotalDiscount()
            );

            assertTrue(
                    "El preu unitari hauria de ser igual o superior a 0.",
                    line.getUnitPrice() >= 0
            );

            assertTrue(
                    "El total de preu hauria de ser igual o superior a 0.",
                    line.getTotalPrice() >= 0
            );

            assertTrue(
                    "El total d'IVA hauria de ser igual o superior a 0.",
                    line.getTotalTax() >= 0
            );

            assertTrue(
                    "El marge cooperatiu hauria de ser igual o superior a 0.",
                    line.getTotalCooperativeMargin() >= 0
            );

            assertTrue(
                    "El descompte total hauria de ser igual o superior a 0.",
                    line.getTotalDiscount() >= 0
            );
        }
    }

    /**
     * Comprova si una llista conté una comanda amb l'identificador indicat.
     *
     * @param orders llista de comandes
     * @param orderId identificador buscat
     * @return true si la comanda existeix a la llista
     */
    private boolean containsOrderWithId(List<ManagedOrder> orders, Long orderId) {
        for (ManagedOrder order : orders) {
            if (order != null && orderId.equals(order.getId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Comprova si un valor numèric està dins d'un rang.
     *
     * @param value valor a comprovar
     * @param min valor mínim
     * @param max valor màxim
     * @return true si el valor està dins del rang
     */
    private boolean isBetween(Double value, Double min, Double max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * Normalitza una data rebuda del backend.
     *
     * @param date data en format text
     * @return data en format yyyy-MM-dd
     */
    private String normalizeDate(String date) {
        if (date.length() > 10) {
            return date.substring(0, 10);
        }

        return date;
    }

    /**
     * Comprova si un text té contingut.
     *
     * @param text text a comprovar
     * @return true si el text no és null ni buit
     */
    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
