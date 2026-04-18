package agrotech.client.retrofit;

import agrotech.dto.order.OrderListResponseDto;
import agrotech.dto.order.OrderRequestDto;
import agrotech.dto.order.OrderResponseDto;
import agrotech.dto.user.ApiMessageResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Defineix les peticions del backend per a la gestió de comandes.
 *
 * @author Javier Santana Coronado
 */
public interface OrdersApi {

    /**
     * Crea una nova comanda.
     *
     * @param request dades de la comanda
     * @return resposta amb la comanda creada
     */
    @POST("api/orders/create")
    Call<OrderResponseDto> createOrder(@Body OrderRequestDto request);

    /**
     * Elimina una comanda existent.
     *
     * @param orderId identificador de la comanda
     * @return resposta del backend
     */
    @DELETE("api/orders/delete/{orderId}")
    Call<ApiMessageResponseDto> deleteOrder(@Path("orderId") Long orderId);

    /**
     * Consulta una comanda concreta pel seu identificador.
     *
     * @param orderId identificador de la comanda
     * @return resposta amb la comanda consultada
     */
    @GET("api/orders/view/{orderId}")
    Call<OrderResponseDto> getOrder(@Path("orderId") Long orderId);

    /**
     * Consulta les comandes de l'usuari autenticat.
     *
     * @return resposta amb la llista de comandes
     */
    @GET("api/orders/my-orders")
    Call<OrderListResponseDto> getMyOrders();

    /**
     * Consulta comandes aplicant filtres.
     *
     * @param customerId identificador del client (opcional)
     * @param poblacio població de destí (opcional)
     * @param startDate data d'inici (opcional)
     * @param endDate data de fi (opcional)
     * @param minTotal mínim total de la comanda (opcional)
     * @param maxTotal màxim total de la comanda (opcional)
     * @param minCooperativeMargin mínim marge cooperatiu (opcional)
     * @param maxCooperativeMargin màxim marge cooperatiu (opcional)
     * @param minOrderTax mínim d'impostos (opcional)
     * @param maxOrderTax màxim d'impostos (opcional)
     * @return resposta amb la llista de comandes
     */
    @GET("api/orders/filter")
    Call<OrderListResponseDto> getOrdersByFilters(
            @Query("customerId") Long customerId,
            @Query("poblacio") String poblacio,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("minTotal") Double minTotal,
            @Query("maxTotal") Double maxTotal,
            @Query("minCooperativeMargin") Double minCooperativeMargin,
            @Query("maxCooperativeMargin") Double maxCooperativeMargin,
            @Query("minOrderTax") Double minOrderTax,
            @Query("maxOrderTax") Double maxOrderTax
    );
}
