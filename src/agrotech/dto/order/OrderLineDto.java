package agrotech.dto.order;

/**
 * Representa una línia de comanda rebuda del backend.
 *
 * @author Javier Santana Coronado
 */
public class OrderLineDto {

    private Long id;
    private String productCategory;
    private Integer quantity;
    private Double unitPrice;
    private Double totalTax;
    private Double totalPrice;
    private Double totalDiscount;
    private Double totalCooperativeMargin;

    /**
     * Retorna l'identificador de la línia.
     *
     * @return identificador de la línia
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna la categoria del producte.
     *
     * @return categoria del producte
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * Retorna la quantitat demanada.
     *
     * @return quantitat demanada
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Retorna el preu unitari.
     *
     * @return preu unitari
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Retorna el total d'impostos de la línia.
     *
     * @return total d'impostos
     */
    public Double getTotalTax() {
        return totalTax;
    }

    /**
     * Retorna el total de la línia.
     *
     * @return total de la línia
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retorna el descompte total aplicat a la línia.
     *
     * @return descompte total de la línia
     */
    public Double getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * Retorna el marge cooperatiu total de la línia.
     *
     * @return marge cooperatiu total
     */
    public Double getTotalCooperativeMargin() {
        return totalCooperativeMargin;
    }
}
