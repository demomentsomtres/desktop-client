package agrotech.dto.order;

import java.util.List;

/**
 * Representa una comanda rebuda del backend.
 *
 * @author Javier Santana Coronado
 */
public class OrderDto {

    private Long id;
    private String customerId;
    private String date;
    private Double totalOrderPrice;
    private Double totalOrderCooperativeMargin;
    private Double totalOrderTax;
    private Double totalDiscount;
    private String poblacioDesti;
    private List<OrderLineDto> orderLineResponse;

    /**
     * Retorna l'identificador de la comanda.
     *
     * @return identificador de la comanda
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna l'identificador del client.
     *
     * @return identificador del client
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Retorna la data de la comanda.
     *
     * @return data de la comanda
     */
    public String getDate() {
        return date;
    }

    /**
     * Retorna el preu total de la comanda.
     *
     * @return preu total de la comanda
     */
    public Double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    /**
     * Retorna el marge cooperatiu total.
     *
     * @return marge cooperatiu total
     */
    public Double getTotalOrderCooperativeMargin() {
        return totalOrderCooperativeMargin;
    }

    /**
     * Retorna el total d'impostos.
     *
     * @return total d'impostos
     */
    public Double getTotalOrderTax() {
        return totalOrderTax;
    }

    /**
     * Retorna el descompte total de la comanda.
     *
     * @return descompte total
     */
    public Double getTotalDiscount() {
        return totalDiscount;
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
     * Retorna les línies de la comanda.
     *
     * @return línies de la comanda
     */
    public List<OrderLineDto> getOrderLineResponse() {
        return orderLineResponse;
    }
}
