package agrotech.dto.order;

import java.util.List;

/**
 * Representa la resposta del backend quan retorna una llista de comandes.
 *
 * @author Javier Santana Coronado
 */
public class OrderListResponseDto {

    private int status;
    private String message;
    private int total;
    private List<OrderDto> orders;

    /**
     * Retorna el codi d'estat.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
    }

    /**
     * Retorna el missatge de la resposta.
     *
     * @return missatge de la resposta
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retorna el total de comandes.
     *
     * @return total de comandes
     */
    public int getTotal() {
        return total;
    }

    /**
     * Retorna la llista de comandes.
     *
     * @return llista de comandes
     */
    public List<OrderDto> getOrders() {
        return orders;
    }
}
