package agrotech.service;

import agrotech.client.ClientResponse;
import agrotech.client.OrderApiClient;
import agrotech.dto.order.OrderLineRequestDto;
import agrotech.dto.order.OrderRequestDto;
import agrotech.model.ManagedOrder;
import java.util.List;

/**
 * Gestiona la lògica de negoci del mòdul de comandes.
 *
 * @author Javier Santana Coronado
 */
public class OrdersService {

    private final OrderApiClient orderApiClient;

    /**
     * Crea el servei de gestió de comandes.
     */
    public OrdersService() {
        this.orderApiClient = new OrderApiClient();
    }

    /**
     * Consulta les comandes de l'usuari autenticat.
     *
     * @return resposta amb la llista de comandes
     */
    public ClientResponse<List<ManagedOrder>> getMyOrders() {
        return orderApiClient.getMyOrders();
    }

    /**
     * Consulta una comanda concreta pel seu identificador.
     *
     * @param orderId identificador de la comanda
     * @return resposta amb la comanda consultada
     */
    public ClientResponse<ManagedOrder> getOrder(Long orderId) {
        if (orderId == null) {
            return new ClientResponse<>(false,
                    "Cal indicar un identificador de comanda.", null);
        }

        return orderApiClient.getOrder(orderId);
    }

    /**
     * Consulta comandes aplicant filtres.
     *
     * @param customerId identificador del client
     * @param poblacio població de destí
     * @param startDate data d'inici
     * @param endDate data de fi
     * @param minTotal mínim total
     * @param maxTotal màxim total
     * @param minCooperativeMargin mínim marge cooperatiu
     * @param maxCooperativeMargin màxim marge cooperatiu
     * @param minOrderTax mínim d'impostos
     * @param maxOrderTax màxim d'impostos
     * @return resposta amb la llista de comandes
     */
    public ClientResponse<List<ManagedOrder>> getOrdersByFilters(
            Long customerId, String poblacio,
            String startDate, String endDate,
            Double minTotal, Double maxTotal,
            Double minCooperativeMargin, Double maxCooperativeMargin,
            Double minOrderTax, Double maxOrderTax
    ) {
        return orderApiClient.getOrdersByFilters(
                customerId, normalize(poblacio),
                normalize(startDate), normalize(endDate),
                minTotal, maxTotal,
                minCooperativeMargin, maxCooperativeMargin,
                minOrderTax, maxOrderTax
        );
    }

    /**
     * Crea una nova comanda.
     *
     * @param poblacioDesti població de destí
     * @param orderLines llista de línies
     * @return resposta amb la comanda creada
     */
    public ClientResponse<ManagedOrder> createOrder(
            String poblacioDesti,
            List<OrderLineRequestDto> orderLines
    ) {
        String normalizedPoblacio = normalize(poblacioDesti);

        if (normalizedPoblacio.isEmpty()) {
            return new ClientResponse<>(false,
                    "La població de destí és obligatòria.", null);
        }

        if (orderLines == null || orderLines.isEmpty()) {
            return new ClientResponse<>(false,
                    "Cal afegir com a mínim una línia a la comanda.", null);
        }

        OrderRequestDto request = new OrderRequestDto(normalizedPoblacio, orderLines);

        return orderApiClient.createOrder(request);
    }

    /**
     * Elimina una comanda existent.
     *
     * @param orderId identificador de la comanda
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteOrder(Long orderId) {
        if (orderId == null) {
            return new ClientResponse<>(false,
                    "Cal seleccionar una comanda.", null);
        }

        return orderApiClient.deleteOrder(orderId);
    }

    /**
     * Normalitza un text del formulari.
     *
     * @param text text original
     * @return text netejat o null si és buit
     */
    private String normalize(String text) {
        if (text == null) {
            return null;
        }

        String trimmed = text.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
