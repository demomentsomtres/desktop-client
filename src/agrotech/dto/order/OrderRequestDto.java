package agrotech.dto.order;

import java.util.List;

/**
 * Representa el cos de la petició per crear una comanda.
 *
 * @author Javier Santana Coronado
 */
public class OrderRequestDto {

    private String poblacioDesti;
    private List<OrderLineRequestDto> orderLineRequest;

    /**
     * Crea una petició amb les dades indicades.
     *
     * @param poblacioDesti població de destí de la comanda
     * @param orderLineRequest llista de línies de la comanda
     */
    public OrderRequestDto(String poblacioDesti,
            List<OrderLineRequestDto> orderLineRequest) {
        this.poblacioDesti = poblacioDesti;
        this.orderLineRequest = orderLineRequest;
    }

    /**
     * Retorna la població de destí.
     *
     * @return població de destí
     */
    public String getPoblacioDesti() {
        return poblacioDesti;
    }

    /**
     * Retorna la llista de línies.
     *
     * @return llista de línies
     */
    public List<OrderLineRequestDto> getOrderLineRequest() {
        return orderLineRequest;
    }
}
