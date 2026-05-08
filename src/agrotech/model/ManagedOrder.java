package agrotech.model;

import agrotech.dto.order.OrderLineDto;
import java.util.List;

/**
 * Representa una comanda gestionable des de l'aplicació.
 *
 * @author Javier Santana Coronado
 */
public class ManagedOrder {

    private Long id;
    private String customerId;
    private String date;
    private String poblacioDesti;
    private Double totalOrderPrice;
    private Double totalOrderCooperativeMargin;
    private Double totalOrderTax;
    private Double totalDiscount;
    private List<OrderLineDto> orderLines;

    /**
     * Crea una comanda amb les dades indicades.
     *
     * @param id identificador de la comanda
     * @param customerId identificador del client
     * @param date data de la comanda
     * @param poblacioDesti població de destí
     * @param totalOrderPrice total de la comanda amb descompte i sense IVA
     * @param totalOrderCooperativeMargin total del marge cooperatiu en euros
     * @param totalOrderTax import total de l'IVA en euros
     * @param totalDiscount descompte total aplicat a la comanda
     * @param orderLines llista de línies de la comanda
     */
    public ManagedOrder(
            Long id,
            String customerId,
            String date,
            String poblacioDesti,
            Double totalOrderPrice,
            Double totalOrderCooperativeMargin,
            Double totalOrderTax,
            Double totalDiscount,
            List<OrderLineDto> orderLines
    ) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.poblacioDesti = poblacioDesti;
        this.totalOrderPrice = totalOrderPrice;
        this.totalOrderCooperativeMargin = totalOrderCooperativeMargin;
        this.totalOrderTax = totalOrderTax;
        this.totalDiscount = totalDiscount;
        this.orderLines = orderLines;
    }

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
     * Retorna la població de destí.
     *
     * @return població de destí
     */
    public String getPoblacioDesti() {
        return poblacioDesti;
    }

    /**
     * Retorna el total de la comanda amb descompte aplicat i sense IVA.
     *
     * @return total de la comanda sense IVA
     */
    public Double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    /**
     * Retorna l'import total del marge cooperatiu en euros.
     *
     * @return import total del marge cooperatiu
     */
    public Double getTotalOrderCooperativeMargin() {
        return totalOrderCooperativeMargin;
    }

    /**
     * Retorna l'import total de l'IVA.
     *
     * @return import total de l'IVA
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
     * Retorna la llista de línies de la comanda.
     *
     * @return llista de línies
     */
    public List<OrderLineDto> getOrderLines() {
        return orderLines;
    }

    @Override
    public String toString() {
        return "ManagedOrder{"
                + "id=" + id
                + ", customerId='" + customerId + '\''
                + ", date='" + date + '\''
                + ", poblacioDesti='" + poblacioDesti + '\''
                + ", totalOrderPrice=" + totalOrderPrice
                + '}';
    }
}
