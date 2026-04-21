package agrotech.service;

import agrotech.client.ClientResponse;
import agrotech.model.ManagedOrder;
import agrotech.session.SessionManager;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves unitàries bàsiques de la classe OrdersService.
 *
 * Es comprova la validació de paràmetres del servei de comandes sense
 * necessitat de connexió amb el servidor.
 *
 * @author Javier Santana Coronado
 */
public class OrdersServiceTest {

    private OrdersService ordersService;

    @Before
    public void setUp() {
        SessionManager.logout();
        ordersService = new OrdersService();
    }

    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que createOrder falla si la població de destí és buida.
     */
    @Test
    public void createOrder_poblacioDestiIBuida_retornaError() {
        ClientResponse<ManagedOrder> response
                = ordersService.createOrder("", new ArrayList<>());

        assertFalse("Hauria de fallar si la població és buida.", response.isSuccess());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
    }

    /**
     * Comprova que createOrder falla si la població de destí és null.
     */
    @Test
    public void createOrder_poblacioDestiNull_retornaError() {
        ClientResponse<ManagedOrder> response
                = ordersService.createOrder(null, new ArrayList<>());

        assertFalse("Hauria de fallar si la població és null.", response.isSuccess());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
    }

    /**
     * Comprova que createOrder falla si la llista de línies és buida.
     */
    @Test
    public void createOrder_lliniesIBuides_retornaError() {
        ClientResponse<ManagedOrder> response
                = ordersService.createOrder("Barcelona", new ArrayList<>());

        assertFalse("Hauria de fallar si les línies estan buides.", response.isSuccess());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
    }

    /**
     * Comprova que createOrder falla si la llista de línies és null.
     */
    @Test
    public void createOrder_lliniesNull_retornaError() {
        ClientResponse<ManagedOrder> response
                = ordersService.createOrder("Barcelona", null);

        assertFalse("Hauria de fallar si les línies són null.", response.isSuccess());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
    }

    /**
     * Comprova que deleteOrder falla si l'identificador de comanda és null.
     */
    @Test
    public void deleteOrder_orderIdNull_retornaError() {
        ClientResponse<Void> response = ordersService.deleteOrder(null);

        assertFalse("Hauria de fallar si l'orderId és null.", response.isSuccess());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
    }

    /**
     * Comprova que getOrder falla si l'identificador de comanda és null.
     */
    @Test
    public void getOrder_orderIdNull_retornaError() {
        ClientResponse<ManagedOrder> response = ordersService.getOrder(null);

        assertFalse("Hauria de fallar si l'orderId és null.", response.isSuccess());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
    }
}
