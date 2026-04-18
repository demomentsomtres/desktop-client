package agrotech.dto.order;

/**
 * Representa la resposta del backend quan retorna una comanda concreta.
 *
 * @author Javier Santana Coronado
 */
public class OrderResponseDto {

    private int status;
    private String message;
    private OrderDto order;

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
     * Retorna la comanda retornada.
     *
     * @return comanda retornada
     */
    public OrderDto getOrder() {
        return order;
    }
}
