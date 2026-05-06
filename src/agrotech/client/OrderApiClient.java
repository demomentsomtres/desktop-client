package agrotech.client;

import agrotech.client.retrofit.OrdersApi;
import agrotech.client.retrofit.RetrofitProvider;
import agrotech.dto.order.OrderDto;
import agrotech.dto.order.OrderListResponseDto;
import agrotech.dto.order.OrderRequestDto;
import agrotech.dto.order.OrderResponseDto;
import agrotech.dto.user.ApiMessageResponseDto;
import agrotech.model.ManagedOrder;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Gestiona les peticions de comandes contra el backend.
 *
 * @author Javier Santana Coronado
 */
public class OrderApiClient {

    private final OrdersApi ordersApi;
    private final Gson gson;

    /**
     * Crea el client de gestió de comandes.
     */
    public OrderApiClient() {
        this.ordersApi = RetrofitProvider.getInstance().create(OrdersApi.class);
        this.gson = new Gson();
    }

    /**
     * Consulta les comandes de l'usuari autenticat.
     *
     * @return resposta amb la llista de comandes
     */
    public ClientResponse<List<ManagedOrder>> getMyOrders() {
        try {
            Call<OrderListResponseDto> call = ordersApi.getMyOrders();
            Response<OrderListResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                OrderListResponseDto body = response.body();

                if (body == null || body.getOrders() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                List<ManagedOrder> orders = mapOrders(body.getOrders());
                return new ClientResponse<>(true,
                        "Consulta de comandes correcta.", orders);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Consulta una comanda concreta pel seu identificador.
     *
     * @param orderId identificador de la comanda
     * @return resposta amb la comanda consultada
     */
    public ClientResponse<ManagedOrder> getOrder(Long orderId) {
        try {
            Call<OrderResponseDto> call = ordersApi.getOrder(orderId);
            Response<OrderResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                OrderResponseDto body = response.body();

                if (body == null || body.getOrder() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                ManagedOrder order = mapOrder(body.getOrder());
                return new ClientResponse<>(true,
                        "Consulta de comanda correcta.", order);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
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
        try {
            Call<OrderListResponseDto> call = ordersApi.getOrdersByFilters(
                    customerId, poblacio, startDate, endDate,
                    minTotal, maxTotal, minCooperativeMargin, maxCooperativeMargin,
                    minOrderTax, maxOrderTax
            );
            Response<OrderListResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                OrderListResponseDto body = response.body();

                if (body == null || body.getOrders() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                List<ManagedOrder> orders = mapOrders(body.getOrders());
                return new ClientResponse<>(true,
                        "Consulta de comandes correcta.", orders);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Crea una nova comanda.
     *
     * @param request dades de la comanda
     * @return resposta amb la comanda creada
     */
    public ClientResponse<ManagedOrder> createOrder(OrderRequestDto request) {
        try {
            Call<OrderResponseDto> call = ordersApi.createOrder(request);
            Response<OrderResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                OrderResponseDto body = response.body();

                if (body == null || body.getOrder() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                ManagedOrder order = mapOrder(body.getOrder());
                return new ClientResponse<>(true,
                        "Comanda creada correctament.", order);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Elimina una comanda existent.
     *
     * @param orderId identificador de la comanda
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteOrder(Long orderId) {
        try {
            Call<ApiMessageResponseDto> call = ordersApi.deleteOrder(orderId);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Comanda eliminada correctament.");
                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Converteix una llista de DTOs a una llista de models del client.
     *
     * @param orderDtos llista de DTOs
     * @return llista de models
     */
    private List<ManagedOrder> mapOrders(List<OrderDto> orderDtos) {
        List<ManagedOrder> orders = new ArrayList<>();

        for (OrderDto dto : orderDtos) {
            ManagedOrder order = mapOrder(dto);
            if (order != null) {
                orders.add(order);
            }
        }

        return orders;
    }

    /**
     * Converteix un DTO a un model del client.
     *
     * @param dto DTO retornat pel backend
     * @return model del client o {@code null} si no és vàlid
     */
    private ManagedOrder mapOrder(OrderDto dto) {
        if (dto == null || dto.getId() == null) {
            return null;
        }

        return new ManagedOrder(
                dto.getId(),
                dto.getCustomerId(),
                dto.getDate(),
                dto.getPoblacioDesti(),
                dto.getTotalOrderPrice(),
                dto.getTotalOrderCooperativeMargin(),
                dto.getTotalOrderTax(),
                dto.getTotalDiscount(),
                dto.getOrderLineResponse()
        );
    }

    /**
     * Extreu el missatge d'èxit d'una resposta.
     *
     * @param body cos de la resposta
     * @param defaultMessage missatge per defecte
     * @return missatge final
     */
    private String extractSuccessMessage(ApiMessageResponseDto body,
            String defaultMessage) {
        if (body != null && body.getMessage() != null
                && !body.getMessage().isBlank()) {
            return body.getMessage();
        }

        return defaultMessage;
    }

    /**
     * Extreu el missatge d'error d'una resposta HTTP.
     *
     * @param response resposta HTTP
     * @return missatge d'error
     */
    private String extractErrorMessage(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();

                if (errorJson != null && !errorJson.isBlank()) {
                    ApiMessageResponseDto errorResponse
                            = gson.fromJson(errorJson, ApiMessageResponseDto.class);

                    if (errorResponse != null
                            && errorResponse.getMessage() != null
                            && !errorResponse.getMessage().isBlank()) {
                        return errorResponse.getMessage();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error del servidor. Codi HTTP: " + response.code();
    }
}
